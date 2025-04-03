package nl.mtvehicles.core.infrastructure.dataconfig;

import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.enums.ConfigType;
import nl.mtvehicles.core.infrastructure.enums.VehicleType;
import nl.mtvehicles.core.infrastructure.models.MTVConfig;
import nl.mtvehicles.core.infrastructure.utils.ItemUtils;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Methods for vehicle data storage in SQLite.
 * Do not initialise this class directly. Use {@link ConfigModule#vehicleDataConfig} instead.
 */
public class VehicleDataConfig extends MTVConfig {

    private Connection connection;
    private final Map<String, Map<Option, Object>> vehicleDataInMemory = new ConcurrentHashMap<>();
    private final Queue<Runnable> saveQueue = new LinkedList<>();
    private BukkitRunnable saveTask;
    private boolean isInitialized = false;
    private final Map<String, Connection> userConnections = new ConcurrentHashMap<>();
    private final File userDataDir = new File(Main.instance.getDataFolder(), "userdata");

    /**
     * Default constructor - do not use this.
     * Use {@link ConfigModule#vehicleDataConfig} instead.
     */
    public VehicleDataConfig() {
        super(ConfigType.VEHICLE_DATA);
        initializeDatabase();
        loadFromDatabase();
        startAutoSaveTask();
    }

    private void migrateOldData() {
        if (connection == null) return;

        try {
            // Load all vehicles from old database
            try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM vehicles")) {
                ResultSet rs = stmt.executeQuery();

                int migratedCount = 0;
                while (rs.next()) {
                    String licensePlate = rs.getString("license_plate");
                    UUID ownerUuid = UUID.fromString(rs.getString("owner_uuid"));

                    // Get or create user database connection
                    Connection userConn = getUserConnection(ownerUuid);
                    userConn.setAutoCommit(false);

                    try {
                        // Insert vehicle
                        try (PreparedStatement insertStmt = userConn.prepareStatement(
                                "INSERT INTO vehicles VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

                            insertStmt.setString(1, rs.getString("license_plate"));
                            insertStmt.setString(2, rs.getString("owner_uuid"));
                            insertStmt.setString(3, rs.getString("vehicle_type"));
                            insertStmt.setString(4, rs.getString("name"));
                            insertStmt.setString(5, rs.getString("skin_item"));
                            insertStmt.setInt(6, rs.getInt("skin_damage"));
                            insertStmt.setBoolean(7, rs.getBoolean("fuel_enabled"));
                            insertStmt.setDouble(8, rs.getDouble("fuel"));
                            insertStmt.setDouble(9, rs.getDouble("fuel_usage"));
                            insertStmt.setDouble(10, rs.getDouble("braking_speed"));
                            insertStmt.setDouble(11, rs.getDouble("friction_speed"));
                            insertStmt.setDouble(12, rs.getDouble("acceleration_speed"));
                            insertStmt.setDouble(13, rs.getDouble("max_speed"));
                            insertStmt.setDouble(14, rs.getDouble("max_speed_backwards"));
                            insertStmt.setDouble(15, rs.getDouble("rotation_speed"));
                            insertStmt.setBoolean(16, rs.getBoolean("trunk_enabled"));
                            insertStmt.setInt(17, rs.getInt("trunk_rows"));
                            insertStmt.setBoolean(18, rs.getBoolean("is_open"));
                            insertStmt.setBoolean(19, rs.getBoolean("is_glowing"));
                            insertStmt.setBoolean(20, rs.getBoolean("horn_enabled"));
                            insertStmt.setDouble(21, rs.getDouble("health"));
                            insertStmt.setString(22, rs.getString("nbt_value"));
                            insertStmt.setBoolean(23, rs.getBoolean("is_public"));

                            insertStmt.executeUpdate();
                        }

                        // Copy members
                        try (PreparedStatement oldMembers = connection.prepareStatement(
                                "SELECT * FROM vehicle_members WHERE license_plate = ?")) {

                            oldMembers.setString(1, licensePlate);
                            ResultSet membersRs = oldMembers.executeQuery();

                            try (PreparedStatement newMembers = userConn.prepareStatement(
                                    "INSERT INTO vehicle_members VALUES (?, ?, ?)")) {

                                while (membersRs.next()) {
                                    newMembers.setString(1, membersRs.getString("license_plate"));
                                    newMembers.setString(2, membersRs.getString("member_uuid"));
                                    newMembers.setBoolean(3, membersRs.getBoolean("is_rider"));
                                    newMembers.addBatch();
                                }
                                newMembers.executeBatch();
                            }
                        }

                        // Copy trunk items
                        try (PreparedStatement oldTrunk = connection.prepareStatement(
                                "SELECT * FROM vehicle_trunk WHERE license_plate = ? ORDER BY item_index")) {

                            oldTrunk.setString(1, licensePlate);
                            ResultSet trunkRs = oldTrunk.executeQuery();

                            try (PreparedStatement newTrunk = userConn.prepareStatement(
                                    "INSERT INTO vehicle_trunk VALUES (?, ?, ?)")) {

                                while (trunkRs.next()) {
                                    newTrunk.setString(1, trunkRs.getString("license_plate"));
                                    newTrunk.setInt(2, trunkRs.getInt("item_index"));
                                    newTrunk.setString(3, trunkRs.getString("item_data"));
                                    newTrunk.addBatch();
                                }
                                newTrunk.executeBatch();
                            }
                        }

                        userConn.commit();
                        migratedCount++;
                    } catch (SQLException e) {
                        userConn.rollback();
                        Main.logSevere("Failed to migrate vehicle " + licensePlate + ": " + e.getMessage());
                    } finally {
                        userConn.setAutoCommit(true);
                    }
                }

                Main.logInfo("Successfully migrated " + migratedCount + " vehicles to per-user storage");
            }

            // Mark old database as migrated
            connection.close();
            connection = null;
            File oldFile = new File(Main.instance.getDataFolder(), "data/vehicles.db");
            File migratedFile = new File(Main.instance.getDataFolder(), "data/vehicles.db.migrated");
            if (!oldFile.renameTo(migratedFile)) {
                Main.logWarning("Failed to rename old database file after migration");
            }
        } catch (SQLException e) {
            Main.logSevere("Failed to migrate old database: " + e.getMessage());
        }
    }

    /**
     * Initialize SQLite database connection
     */
    private void initializeDatabase() {
        try {
            // Create user data directory if needed
            if (!userDataDir.exists() && !userDataDir.mkdirs()) {
                throw new IOException("Failed to create user data directory");
            }

            // Initialize SQLite driver
            Class.forName("org.sqlite.JDBC");

            // Check for old database format
            File oldDbFile = new File(Main.instance.getDataFolder(), "data/vehicles.db");
            if (oldDbFile.exists()) {
                connection = DriverManager.getConnection("jdbc:sqlite:" + oldDbFile.getAbsolutePath());
                migrateOldData();
            }

            // Initialize user databases
            File[] userFiles = userDataDir.listFiles((dir, name) -> name.endsWith(".db"));
            if (userFiles != null) {
                for (File userFile : userFiles) {
                    try {
                        String uuidStr = userFile.getName().replace(".db", "");
                        UUID userUuid = UUID.fromString(uuidStr);
                        Connection userConn = DriverManager.getConnection("jdbc:sqlite:" + userFile.getAbsolutePath());

                        // Initialize tables
                        try (Statement stmt = userConn.createStatement()) {
                            stmt.execute("PRAGMA journal_mode = WAL");
                            stmt.execute("PRAGMA synchronous = NORMAL");
                            stmt.execute("PRAGMA foreign_keys = ON");

                            stmt.execute("CREATE TABLE IF NOT EXISTS vehicles (" +
                                    "license_plate TEXT PRIMARY KEY," +
                                    "owner_uuid TEXT NOT NULL," +
                                    "vehicle_type TEXT NOT NULL," +
                                    "name TEXT NOT NULL," +
                                    "skin_item TEXT NOT NULL," +
                                    "skin_damage INTEGER NOT NULL," +
                                    "fuel_enabled BOOLEAN NOT NULL DEFAULT 0," +
                                    "fuel DOUBLE NOT NULL DEFAULT 0," +
                                    "fuel_usage DOUBLE NOT NULL DEFAULT 0," +
                                    "braking_speed DOUBLE NOT NULL DEFAULT 0," +
                                    "friction_speed DOUBLE NOT NULL DEFAULT 0," +
                                    "acceleration_speed DOUBLE NOT NULL DEFAULT 0," +
                                    "max_speed DOUBLE NOT NULL DEFAULT 0," +
                                    "max_speed_backwards DOUBLE NOT NULL DEFAULT 0," +
                                    "rotation_speed DOUBLE NOT NULL DEFAULT 0," +
                                    "trunk_enabled BOOLEAN NOT NULL DEFAULT 0," +
                                    "trunk_rows INTEGER NOT NULL DEFAULT 1," +
                                    "is_open BOOLEAN NOT NULL DEFAULT 0," +
                                    "is_glowing BOOLEAN NOT NULL DEFAULT 0," +
                                    "horn_enabled BOOLEAN NOT NULL DEFAULT 0," +
                                    "health DOUBLE NOT NULL DEFAULT 0," +
                                    "nbt_value TEXT," +
                                    "is_public BOOLEAN NOT NULL DEFAULT 0" +
                                    ")");

                            stmt.execute("CREATE TABLE IF NOT EXISTS vehicle_members (" +
                                    "license_plate TEXT NOT NULL," +
                                    "member_uuid TEXT NOT NULL," +
                                    "is_rider BOOLEAN NOT NULL DEFAULT 0," +
                                    "PRIMARY KEY (license_plate, member_uuid)," +
                                    "FOREIGN KEY (license_plate) REFERENCES vehicles(license_plate) ON DELETE CASCADE" +
                                    ")");

                            stmt.execute("CREATE TABLE IF NOT EXISTS vehicle_trunk (" +
                                    "license_plate TEXT NOT NULL," +
                                    "item_index INTEGER NOT NULL," +
                                    "item_data TEXT NOT NULL," +
                                    "PRIMARY KEY (license_plate, item_index)," +
                                    "FOREIGN KEY (license_plate) REFERENCES vehicles(license_plate) ON DELETE CASCADE" +
                                    ")");
                        }

                        userConnections.put(uuidStr, userConn);
                        loadFromDatabase(userConn);
                    } catch (Exception e) {
                        Main.logSevere("Error initializing user database " + userFile.getName() + ": " + e.getMessage());
                    }
                }
            }

            isInitialized = true;
            Main.logInfo("Vehicle database initialized with " + vehicleDataInMemory.size() + " vehicles");
        } catch (Exception e) {
            Main.logSevere("Failed to initialize vehicle databases: " + e.getMessage());
            isInitialized = false;
        }
    }

    /**
     * Load all vehicle data from database into memory
     */
    public void loadFromDatabase() {
        // This is now just a wrapper that loads from all sources
        if (connection != null) {
            loadFromDatabase(connection);
        }

        // User databases are loaded during initialization
    }

    private void loadFromDatabase(Connection conn) {
        try {
            // Load vehicles
            try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM vehicles")) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String licensePlate = rs.getString("license_plate");
                    Map<Option, Object> vehicleData = new HashMap<>();

                    // Map all vehicle options
                    vehicleData.put(Option.NAME, rs.getString("name"));
                    vehicleData.put(Option.VEHICLE_TYPE, rs.getString("vehicle_type"));
                    vehicleData.put(Option.SKIN_ITEM, rs.getString("skin_item"));
                    vehicleData.put(Option.SKIN_DAMAGE, rs.getInt("skin_damage"));
                    vehicleData.put(Option.OWNER, rs.getString("owner_uuid"));
                    vehicleData.put(Option.FUEL_ENABLED, rs.getBoolean("fuel_enabled"));
                    vehicleData.put(Option.FUEL, rs.getDouble("fuel"));
                    vehicleData.put(Option.FUEL_USAGE, rs.getDouble("fuel_usage"));
                    vehicleData.put(Option.BRAKING_SPEED, rs.getDouble("braking_speed"));
                    vehicleData.put(Option.FRICTION_SPEED, rs.getDouble("friction_speed"));
                    vehicleData.put(Option.ACCELERATION_SPEED, rs.getDouble("acceleration_speed"));
                    vehicleData.put(Option.MAX_SPEED, rs.getDouble("max_speed"));
                    vehicleData.put(Option.MAX_SPEED_BACKWARDS, rs.getDouble("max_speed_backwards"));
                    vehicleData.put(Option.ROTATION_SPEED, rs.getDouble("rotation_speed"));
                    vehicleData.put(Option.TRUNK_ENABLED, rs.getBoolean("trunk_enabled"));
                    vehicleData.put(Option.TRUNK_ROWS, rs.getInt("trunk_rows"));
                    vehicleData.put(Option.IS_OPEN, rs.getBoolean("is_open"));
                    vehicleData.put(Option.IS_GLOWING, rs.getBoolean("is_glowing"));
                    vehicleData.put(Option.HORN_ENABLED, rs.getBoolean("horn_enabled"));
                    vehicleData.put(Option.HEALTH, rs.getDouble("health"));
                    vehicleData.put(Option.NBT_VALUE, rs.getString("nbt_value"));
                    vehicleData.put(Option.IS_PUBLIC, rs.getBoolean("is_public"));

                    // Initialize empty lists for members and riders
                    vehicleData.put(Option.MEMBERS, new ArrayList<String>());
                    vehicleData.put(Option.RIDERS, new ArrayList<String>());
                    vehicleData.put(Option.TRUNK_DATA, new ArrayList<String>());

                    vehicleDataInMemory.put(licensePlate, vehicleData);
                }
            }

            // Load members and riders
            try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM vehicle_members")) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String licensePlate = rs.getString("license_plate");
                    String memberUuid = rs.getString("member_uuid");
                    boolean isRider = rs.getBoolean("is_rider");

                    Map<Option, Object> vehicleData = vehicleDataInMemory.get(licensePlate);
                    if (vehicleData != null) {
                        List<String> members = (List<String>) vehicleData.computeIfAbsent(Option.MEMBERS, k -> new ArrayList<>());
                        List<String> riders = (List<String>) vehicleData.computeIfAbsent(Option.RIDERS, k -> new ArrayList<>());

                        members.add(memberUuid);
                        if (isRider) {
                            riders.add(memberUuid);
                        }
                    }
                }
            }

            // Load trunk data
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT license_plate, item_index, item_data FROM vehicle_trunk")) {

                ResultSet rs = stmt.executeQuery();
                Map<String, List<ItemStack>> trunkItemsMap = new HashMap<>();

                while (rs.next()) {
                    String licensePlate = rs.getString("license_plate");
                    int index = rs.getInt("item_index");
                    String itemData = rs.getString("item_data");

                    List<ItemStack> items = trunkItemsMap.computeIfAbsent(licensePlate, k -> new ArrayList<>());
                    while (items.size() <= index) {
                        items.add(null);
                    }
                    items.set(index, ItemUtils.deserializeItemStack(itemData));
                }

                // Store loaded items in vehicle data
                for (Map.Entry<String, List<ItemStack>> entry : trunkItemsMap.entrySet()) {
                    Map<Option, Object> vehicleData = vehicleDataInMemory.get(entry.getKey());
                    if (vehicleData != null) {
                        vehicleData.put(Option.TRUNK_DATA, entry.getValue());
                    }
                }
            }
        } catch (SQLException e) {
            Main.logSevere("Failed to load vehicle data from database: " + e.getMessage());
        }
    }

    /**
     * Start a task to save data to database periodically
     */
    private void startAutoSaveTask() {
        // Cancel existing task if running
        if (saveTask != null && !saveTask.isCancelled()) {
            saveTask.cancel();
        }

        saveTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!isInitialized) {
                    Main.logWarning("Skipping auto-save - database not initialized");
                    return;
                }

                long startTime = System.currentTimeMillis();
                Main.logInfo("Starting vehicle data auto-save...");

                try {
                    // Process any pending saves first
                    processSaveQueue();

                    // Then do a full save
                    saveToDatabase();

                    long duration = System.currentTimeMillis() - startTime;
                    Main.logInfo(String.format("Auto-save completed in %d ms (%d vehicles saved)",
                            duration, vehicleDataInMemory.size()));
                } catch (Exception e) {
                    Main.logSevere("Error during auto-save: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        };

        // Save every 10 minutes (20 ticks/sec * 60 sec/min * 10 min)
        saveTask.runTaskTimerAsynchronously(Main.instance, 12000, 12000);
        Main.logInfo("Auto-save task scheduled to run every 10 minutes");
    }

    /**
     * Process all pending save operations
     */
    public void processSaveQueue() {
        if (!isInitialized) {
            Main.logWarning("Skipping save queue processing - database not initialized");
            return;
        }

        synchronized (saveQueue) {
            while (!saveQueue.isEmpty()) {
                Runnable saveOperation = saveQueue.poll();
                try {
                    saveOperation.run();
                } catch (Exception e) {
                    Main.logSevere("Error executing save operation: " + e.getMessage());
                }
            }
        }
    }

    private Connection getUserConnection(UUID userUuid) throws SQLException {
        String uuidStr = userUuid.toString();

        // Return existing connection if available
        if (userConnections.containsKey(uuidStr)) {
            Connection conn = userConnections.get(uuidStr);
            if (conn != null && !conn.isClosed()) {
                return conn;
            }
            // Remove stale connection
            userConnections.remove(uuidStr);
        }

        // Create new database file if it doesn't exist
        File dbFile = new File(userDataDir, uuidStr + ".db");
        if (!dbFile.exists()) {
            try {
                if (!dbFile.createNewFile()) {
                    throw new IOException("Failed to create database file");
                }
            } catch (IOException e) {
                throw new SQLException("Failed to create database file: " + e.getMessage());
            }
        }

        // Create new connection
        String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        Connection conn = DriverManager.getConnection(url);

        // Initialize tables if needed
        try (Statement stmt = conn.createStatement()) {
            // Set PRAGMAs for better performance
            stmt.execute("PRAGMA journal_mode = WAL");
            stmt.execute("PRAGMA synchronous = NORMAL");
            stmt.execute("PRAGMA foreign_keys = ON");

            // Create vehicles table
            stmt.execute("CREATE TABLE IF NOT EXISTS vehicles (" +
                    "license_plate TEXT PRIMARY KEY," +
                    "owner_uuid TEXT NOT NULL," +
                    "vehicle_type TEXT NOT NULL," +
                    "name TEXT NOT NULL," +
                    "skin_item TEXT NOT NULL," +
                    "skin_damage INTEGER NOT NULL," +
                    "fuel_enabled BOOLEAN NOT NULL DEFAULT 0," +
                    "fuel DOUBLE NOT NULL DEFAULT 0," +
                    "fuel_usage DOUBLE NOT NULL DEFAULT 0," +
                    "braking_speed DOUBLE NOT NULL DEFAULT 0," +
                    "friction_speed DOUBLE NOT NULL DEFAULT 0," +
                    "acceleration_speed DOUBLE NOT NULL DEFAULT 0," +
                    "max_speed DOUBLE NOT NULL DEFAULT 0," +
                    "max_speed_backwards DOUBLE NOT NULL DEFAULT 0," +
                    "rotation_speed DOUBLE NOT NULL DEFAULT 0," +
                    "trunk_enabled BOOLEAN NOT NULL DEFAULT 0," +
                    "trunk_rows INTEGER NOT NULL DEFAULT 1," +
                    "is_open BOOLEAN NOT NULL DEFAULT 0," +
                    "is_glowing BOOLEAN NOT NULL DEFAULT 0," +
                    "horn_enabled BOOLEAN NOT NULL DEFAULT 0," +
                    "health DOUBLE NOT NULL DEFAULT 0," +
                    "nbt_value TEXT," +
                    "is_public BOOLEAN NOT NULL DEFAULT 0" +
                    ")");

            // Create vehicle_members table
            stmt.execute("CREATE TABLE IF NOT EXISTS vehicle_members (" +
                    "license_plate TEXT NOT NULL," +
                    "member_uuid TEXT NOT NULL," +
                    "is_rider BOOLEAN NOT NULL DEFAULT 0," +
                    "PRIMARY KEY (license_plate, member_uuid)," +
                    "FOREIGN KEY (license_plate) REFERENCES vehicles(license_plate) ON DELETE CASCADE" +
                    ")");

            // Create vehicle_trunk table
            stmt.execute("CREATE TABLE IF NOT EXISTS vehicle_trunk (" +
                    "license_plate TEXT NOT NULL," +
                    "item_index INTEGER NOT NULL," +
                    "item_data TEXT NOT NULL," +
                    "PRIMARY KEY (license_plate, item_index)," +
                    "FOREIGN KEY (license_plate) REFERENCES vehicles(license_plate) ON DELETE CASCADE" +
                    ")");
        }

        // Store connection
        userConnections.put(uuidStr, conn);
        return conn;
    }

    /**
     * Save all vehicle data from memory to database (async)
     */
    public void saveToDatabase() {
        // Save all vehicles to their respective user databases
        for (Map.Entry<String, Map<Option, Object>> entry : vehicleDataInMemory.entrySet()) {
            String licensePlate = entry.getKey();
            UUID ownerUuid = UUID.fromString((String) entry.getValue().get(Option.OWNER));

            try {
                Connection userConn = getUserConnection(ownerUuid);
                saveVehicleToDatabase(licensePlate, userConn);
            } catch (SQLException e) {
                Main.logSevere("Failed to save vehicle " + licensePlate + ": " + e.getMessage());
            }
        }
    }

    public void saveVehicleToDatabase(String licensePlate, Connection conn) throws SQLException {
        if (conn == null || conn.isClosed()) {
            Main.logSevere("Cannot save vehicle " + licensePlate + " - database connection is invalid");
            return;
        }

        Map<Option, Object> vehicleData = vehicleDataInMemory.get(licensePlate);
        if (vehicleData == null) return;

        boolean autoCommit = conn.getAutoCommit();
        conn.setAutoCommit(false);

        try {
            // Delete existing data
            executeUpdate(conn, "DELETE FROM vehicle_trunk WHERE license_plate = ?", licensePlate);
            executeUpdate(conn, "DELETE FROM vehicle_members WHERE license_plate = ?", licensePlate);
            executeUpdate(conn, "DELETE FROM vehicles WHERE license_plate = ?", licensePlate);

            // Insert vehicle
            String vehicleSql = "INSERT INTO vehicles (" +
                    "license_plate, owner_uuid, vehicle_type, name, skin_item, skin_damage, " +
                    "fuel_enabled, fuel, fuel_usage, braking_speed, friction_speed, acceleration_speed, " +
                    "max_speed, max_speed_backwards, rotation_speed, trunk_enabled, trunk_rows, " +
                    "is_open, is_glowing, horn_enabled, health, nbt_value, is_public" +
                    ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(vehicleSql)) {
                int index = 1;
                stmt.setString(index++, licensePlate);
                stmt.setString(index++, (String) vehicleData.get(Option.OWNER));
                stmt.setString(index++, (String) vehicleData.get(Option.VEHICLE_TYPE));
                stmt.setString(index++, (String) vehicleData.get(Option.NAME));
                stmt.setString(index++, (String) vehicleData.get(Option.SKIN_ITEM));

                // Handle numeric types with null checks
                Object skinDamage = vehicleData.get(Option.SKIN_DAMAGE);
                if (skinDamage instanceof Number) {
                    stmt.setInt(index++, ((Number)skinDamage).intValue());
                } else {
                    stmt.setNull(index++, Types.INTEGER);
                }

                // Handle boolean options with null checks
                Boolean fuelEnabled = (Boolean) vehicleData.get(Option.FUEL_ENABLED);
                stmt.setBoolean(index++, fuelEnabled != null ? fuelEnabled : false);

                Object fuel = vehicleData.get(Option.FUEL);
                if (fuel instanceof Number) {
                    stmt.setDouble(index++, ((Number)fuel).doubleValue());
                } else {
                    stmt.setNull(index++, Types.DOUBLE);
                }

                Object fuelUsage = vehicleData.get(Option.FUEL_USAGE);
                if (fuelUsage instanceof Number) {
                    stmt.setDouble(index++, ((Number)fuelUsage).doubleValue());
                } else {
                    stmt.setNull(index++, Types.DOUBLE);
                }

                Object brakingSpeed = vehicleData.get(Option.BRAKING_SPEED);
                if (brakingSpeed instanceof Number) {
                    stmt.setDouble(index++, ((Number)brakingSpeed).doubleValue());
                } else {
                    stmt.setNull(index++, Types.DOUBLE);
                }

                Object frictionSpeed = vehicleData.get(Option.FRICTION_SPEED);
                if (frictionSpeed instanceof Number) {
                    stmt.setDouble(index++, ((Number)frictionSpeed).doubleValue());
                } else {
                    stmt.setNull(index++, Types.DOUBLE);
                }

                Object accelerationSpeed = vehicleData.get(Option.ACCELERATION_SPEED);
                if (accelerationSpeed instanceof Number) {
                    stmt.setDouble(index++, ((Number)accelerationSpeed).doubleValue());
                } else {
                    stmt.setNull(index++, Types.DOUBLE);
                }

                Object maxSpeed = vehicleData.get(Option.MAX_SPEED);
                if (maxSpeed instanceof Number) {
                    stmt.setDouble(index++, ((Number)maxSpeed).doubleValue());
                } else {
                    stmt.setNull(index++, Types.DOUBLE);
                }

                Object maxSpeedBackwards = vehicleData.get(Option.MAX_SPEED_BACKWARDS);
                if (maxSpeedBackwards instanceof Number) {
                    stmt.setDouble(index++, ((Number)maxSpeedBackwards).doubleValue());
                } else {
                    stmt.setNull(index++, Types.DOUBLE);
                }

                Object rotationSpeed = vehicleData.get(Option.ROTATION_SPEED);
                if (rotationSpeed instanceof Number) {
                    stmt.setDouble(index++, ((Number)rotationSpeed).doubleValue());
                } else {
                    stmt.setNull(index++, Types.DOUBLE);
                }

                Boolean trunkEnabled = (Boolean) vehicleData.get(Option.TRUNK_ENABLED);
                stmt.setBoolean(index++, trunkEnabled != null ? trunkEnabled : false);

                Object trunkRows = vehicleData.get(Option.TRUNK_ROWS);
                if (trunkRows instanceof Number) {
                    stmt.setInt(index++, ((Number)trunkRows).intValue());
                } else {
                    stmt.setNull(index++, Types.INTEGER);
                }

                Boolean isOpen = (Boolean) vehicleData.get(Option.IS_OPEN);
                stmt.setBoolean(index++, isOpen != null ? isOpen : false);

                Boolean isGlowing = (Boolean) vehicleData.get(Option.IS_GLOWING);
                stmt.setBoolean(index++, isGlowing != null ? isGlowing : false);

                Boolean hornEnabled = (Boolean) vehicleData.get(Option.HORN_ENABLED);
                stmt.setBoolean(index++, hornEnabled != null ? hornEnabled : false);

                Object health = vehicleData.get(Option.HEALTH);
                if (health instanceof Number) {
                    stmt.setDouble(index++, ((Number)health).doubleValue());
                } else {
                    stmt.setNull(index++, Types.DOUBLE);
                }

                String nbtValue = (String) vehicleData.get(Option.NBT_VALUE);
                if (nbtValue != null) {
                    stmt.setString(index++, nbtValue);
                } else {
                    stmt.setNull(index++, Types.VARCHAR);
                }

                Boolean isPublic = (Boolean) vehicleData.get(Option.IS_PUBLIC);
                stmt.setBoolean(index++, isPublic != null ? isPublic : false);

                stmt.executeUpdate();
            }

            // Insert members
            List<String> members = (List<String>) vehicleData.get(Option.MEMBERS);
            List<String> riders = (List<String>) vehicleData.get(Option.RIDERS);
            if (members != null && !members.isEmpty()) {
                String memberSql = "INSERT INTO vehicle_members (license_plate, member_uuid, is_rider) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(memberSql)) {
                    for (String memberUuid : members) {
                        stmt.setString(1, licensePlate);
                        stmt.setString(2, memberUuid);
                        stmt.setBoolean(3, riders != null && riders.contains(memberUuid));
                        stmt.addBatch();
                    }
                    stmt.executeBatch();
                }
            }

            // Insert trunk items
            List<ItemStack> trunkItems = (List<ItemStack>) vehicleData.get(Option.TRUNK_DATA);
            if (trunkItems != null && !trunkItems.isEmpty()) {
                String trunkSql = "INSERT INTO vehicle_trunk (license_plate, item_index, item_data) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(trunkSql)) {
                    for (int i = 0; i < trunkItems.size(); i++) {
                        ItemStack item = trunkItems.get(i);
                        if (item != null) {
                            stmt.setString(1, licensePlate);
                            stmt.setInt(2, i);
                            stmt.setString(3, ItemUtils.serializeItemStack(item));
                            stmt.addBatch();
                        }
                    }
                    stmt.executeBatch();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                Main.logSevere("Failed to rollback transaction for vehicle " + licensePlate + ": " + ex.getMessage());
            }
            Main.logSevere("Failed to save vehicle " + licensePlate + ": " + e.getMessage());
            throw e;
        } finally {
            try {
                conn.setAutoCommit(autoCommit);
            } catch (SQLException e) {
                Main.logSevere("Failed to reset auto-commit for vehicle " + licensePlate + ": " + e.getMessage());
            }
        }
    }

    private void executeUpdate(Connection conn, String sql, String licensePlate) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, licensePlate);
            stmt.executeUpdate();
        }
    }

    private void executeDelete(Connection conn, String sql, String licensePlate) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, licensePlate);
            stmt.executeUpdate();
        }
    }

    private void setVehicleParameters(PreparedStatement stmt, Map<Option, Object> vehicleData) throws SQLException {
        stmt.setString(1, (String) vehicleData.get(Option.OWNER));
        stmt.setString(2, (String) vehicleData.get(Option.VEHICLE_TYPE));
        stmt.setString(3, (String) vehicleData.get(Option.NAME));
        stmt.setString(4, (String) vehicleData.get(Option.SKIN_ITEM));
        stmt.setInt(5, (Integer) vehicleData.get(Option.SKIN_DAMAGE));

        // Handle boolean options with null checks
        Boolean fuelEnabled = (Boolean) vehicleData.get(Option.FUEL_ENABLED);
        stmt.setBoolean(6, fuelEnabled != null ? fuelEnabled : false);

        stmt.setDouble(7, (Double) vehicleData.get(Option.FUEL));
        stmt.setDouble(8, (Double) vehicleData.get(Option.FUEL_USAGE));
        stmt.setDouble(9, (Double) vehicleData.get(Option.BRAKING_SPEED));
        stmt.setDouble(10, (Double) vehicleData.get(Option.FRICTION_SPEED));
        stmt.setDouble(11, (Double) vehicleData.get(Option.ACCELERATION_SPEED));
        stmt.setDouble(12, (Double) vehicleData.get(Option.MAX_SPEED));
        stmt.setDouble(13, (Double) vehicleData.get(Option.MAX_SPEED_BACKWARDS));
        stmt.setDouble(14, (Double) vehicleData.get(Option.ROTATION_SPEED));

        Boolean trunkEnabled = (Boolean) vehicleData.get(Option.TRUNK_ENABLED);
        stmt.setBoolean(15, trunkEnabled != null ? trunkEnabled : false);

        stmt.setInt(16, (Integer) vehicleData.get(Option.TRUNK_ROWS));

        Boolean isOpen = (Boolean) vehicleData.get(Option.IS_OPEN);
        stmt.setBoolean(17, isOpen != null ? isOpen : false);

        Boolean isGlowing = (Boolean) vehicleData.get(Option.IS_GLOWING);
        stmt.setBoolean(18, isGlowing != null ? isGlowing : false);

        Boolean hornEnabled = (Boolean) vehicleData.get(Option.HORN_ENABLED);
        stmt.setBoolean(19, hornEnabled != null ? hornEnabled : false);

        stmt.setDouble(20, (Double) vehicleData.get(Option.HEALTH));

        String nbtValue = (String) vehicleData.get(Option.NBT_VALUE);
        if (nbtValue != null) {
            stmt.setString(21, nbtValue);
        } else {
            stmt.setNull(21, Types.VARCHAR);
        }

        Boolean isPublic = (Boolean) vehicleData.get(Option.IS_PUBLIC);
        stmt.setBoolean(22, isPublic != null ? isPublic : false);
    }

    private void saveMembers(Connection conn, String licensePlate, Map<Option, Object> vehicleData) throws SQLException {
        List<String> members = (List<String>) vehicleData.get(Option.MEMBERS);
        List<String> riders = (List<String>) vehicleData.get(Option.RIDERS);

        if (members != null && !members.isEmpty()) {
            String sql = "INSERT INTO vehicle_members (license_plate, member_uuid, is_rider) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (String memberUuid : members) {
                    stmt.setString(1, licensePlate);
                    stmt.setString(2, memberUuid);
                    stmt.setBoolean(3, riders != null && riders.contains(memberUuid));
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }
        }
    }

    private void saveTrunkItems(Connection conn, String licensePlate, Map<Option, Object> vehicleData) throws SQLException {
        List<String> trunkData = (List<String>) vehicleData.get(Option.TRUNK_DATA);

        if (trunkData != null && !trunkData.isEmpty()) {
            String sql = "INSERT INTO vehicle_trunk (license_plate, item_index, item_data) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (int i = 0; i < trunkData.size(); i++) {
                    String itemData = trunkData.get(i);
                    if (itemData != null) {
                        stmt.setString(1, licensePlate);
                        stmt.setInt(2, i);
                        stmt.setString(3, itemData);
                        stmt.addBatch();
                    }
                }
                stmt.executeBatch();
            }
        }
    }

    /**
     * Save all vehicle data to disk (synchronous)
     * @deprecated Use async methods instead
     */
    @Deprecated
    public void saveToDisk() {
        if (!isInitialized) return;

        try {
            connection.setAutoCommit(false);

            // Clear all data first (we'll repopulate it)
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("DELETE FROM vehicles");
                stmt.execute("DELETE FROM vehicle_members");
                stmt.execute("DELETE FROM vehicle_trunk");
            }

            // Save all vehicles
            for (Map.Entry<String, Map<Option, Object>> entry : vehicleDataInMemory.entrySet()) {
                String licensePlate = entry.getKey();
                Map<Option, Object> vehicleData = entry.getValue();

                try (PreparedStatement stmt = connection.prepareStatement(
                        "INSERT INTO vehicles VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

                    // Handle all data types properly
                    stmt.setString(1, licensePlate);
                    stmt.setString(2, (String) vehicleData.get(Option.OWNER));
                    stmt.setString(3, (String) vehicleData.get(Option.VEHICLE_TYPE));
                    stmt.setString(4, (String) vehicleData.get(Option.NAME));
                    stmt.setString(5, (String) vehicleData.get(Option.SKIN_ITEM));

                    // Handle numeric types
                    Object skinDamage = vehicleData.get(Option.SKIN_DAMAGE);
                    if (skinDamage != null) stmt.setInt(6, (skinDamage instanceof Double) ? ((Double) skinDamage).intValue() : (Integer) skinDamage);
                    else stmt.setNull(6, Types.INTEGER);

                    stmt.setBoolean(7, (Boolean) vehicleData.getOrDefault(Option.FUEL_ENABLED, false));

                    Object fuel = vehicleData.get(Option.FUEL);
                    if (fuel != null) stmt.setDouble(8, (fuel instanceof Integer) ? ((Integer) fuel).doubleValue() : (Double) fuel);
                    else stmt.setNull(8, Types.DOUBLE);

                    Object fuelUsage = vehicleData.get(Option.FUEL_USAGE);
                    if (fuelUsage != null) stmt.setDouble(9, (fuelUsage instanceof Integer) ? ((Integer) fuelUsage).doubleValue() : (Double) fuelUsage);
                    else stmt.setNull(9, Types.DOUBLE);

                    Object brakingSpeed = vehicleData.get(Option.BRAKING_SPEED);
                    if (brakingSpeed != null) stmt.setDouble(10, (brakingSpeed instanceof Integer) ? ((Integer) brakingSpeed).doubleValue() : (Double) brakingSpeed);
                    else stmt.setNull(10, Types.DOUBLE);

                    Object frictionSpeed = vehicleData.get(Option.FRICTION_SPEED);
                    if (frictionSpeed != null) stmt.setDouble(11, (frictionSpeed instanceof Integer) ? ((Integer) frictionSpeed).doubleValue() : (Double) frictionSpeed);
                    else stmt.setNull(11, Types.DOUBLE);

                    Object accelerationSpeed = vehicleData.get(Option.ACCELERATION_SPEED);
                    if (accelerationSpeed != null) stmt.setDouble(12, (accelerationSpeed instanceof Integer) ? ((Integer) accelerationSpeed).doubleValue() : (Double) accelerationSpeed);
                    else stmt.setNull(12, Types.DOUBLE);

                    Object maxSpeed = vehicleData.get(Option.MAX_SPEED);
                    if (maxSpeed != null) stmt.setDouble(13, (maxSpeed instanceof Integer) ? ((Integer) maxSpeed).doubleValue() : (Double) maxSpeed);
                    else stmt.setNull(13, Types.DOUBLE);

                    Object maxSpeedBackwards = vehicleData.get(Option.MAX_SPEED_BACKWARDS);
                    if (maxSpeedBackwards != null) stmt.setDouble(14, (maxSpeedBackwards instanceof Integer) ? ((Integer) maxSpeedBackwards).doubleValue() : (Double) maxSpeedBackwards);
                    else stmt.setNull(14, Types.DOUBLE);

                    Object rotationSpeed = vehicleData.get(Option.ROTATION_SPEED);
                    if (rotationSpeed != null) stmt.setDouble(15, (rotationSpeed instanceof Integer) ? ((Integer) rotationSpeed).doubleValue() : (Double) rotationSpeed);
                    else stmt.setNull(15, Types.DOUBLE);

                    stmt.setBoolean(16, (Boolean) vehicleData.getOrDefault(Option.TRUNK_ENABLED, false));

                    Object trunkRows = vehicleData.get(Option.TRUNK_ROWS);
                    if (trunkRows != null) stmt.setInt(17, (trunkRows instanceof Double) ? ((Double) trunkRows).intValue() : (Integer) trunkRows);
                    else stmt.setNull(17, Types.INTEGER);

                    stmt.setBoolean(18, (Boolean) vehicleData.getOrDefault(Option.IS_OPEN, false));
                    stmt.setBoolean(19, (Boolean) vehicleData.getOrDefault(Option.IS_GLOWING, false));
                    stmt.setBoolean(20, (Boolean) vehicleData.getOrDefault(Option.HORN_ENABLED, false));

                    Object health = vehicleData.get(Option.HEALTH);
                    if (health != null) stmt.setDouble(21, (health instanceof Integer) ? ((Integer) health).doubleValue() : (Double) health);
                    else stmt.setNull(21, Types.DOUBLE);

                    stmt.setString(22, (String) vehicleData.get(Option.NBT_VALUE));
                    stmt.setBoolean(23, (Boolean) vehicleData.getOrDefault(Option.IS_PUBLIC, false));

                    stmt.executeUpdate();
                }

                // Save members and riders
                List<String> members = (List<String>) vehicleData.get(Option.MEMBERS);
                List<String> riders = (List<String>) vehicleData.get(Option.RIDERS);

                if (members != null) {
                    try (PreparedStatement stmt = connection.prepareStatement(
                            "INSERT INTO vehicle_members VALUES (?, ?, ?)")) {
                        for (String memberUuid : members) {
                            stmt.setString(1, licensePlate);
                            stmt.setString(2, memberUuid);
                            stmt.setBoolean(3, riders != null && riders.contains(memberUuid));
                            stmt.addBatch();
                        }
                        stmt.executeBatch();
                    }
                }

                // Save trunk data
                List<String> trunkData = (List<String>) vehicleData.get(Option.TRUNK_DATA);
                if (trunkData != null) {
                    try (PreparedStatement stmt = connection.prepareStatement(
                            "INSERT INTO vehicle_trunk VALUES (?, ?, ?)")) {
                        for (int i = 0; i < trunkData.size(); i++) {
                            stmt.setString(1, licensePlate);
                            stmt.setInt(2, i);
                            stmt.setString(3, trunkData.get(i));
                            stmt.addBatch();
                        }
                        stmt.executeBatch();
                    }
                }
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                Main.logSevere("Failed to rollback vehicle database transaction: " + ex.getMessage());
            }
            Main.logSevere("Failed to save vehicle data to database: " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                Main.logSevere("Failed to reset auto-commit on vehicle database: " + e.getMessage());
            }
        }

        saveToDatabase();
    }

    /**
     * Queue a save operation for async execution
     */
    private void queueSaveOperation(Runnable saveOperation) {
        synchronized (saveQueue) {
            saveQueue.add(saveOperation);
        }

        // Save immediately if queue is getting large
        if (saveQueue.size() > 50) {
            processSaveQueue();
        }
    }

    /**
     * Get a data option of a vehicle from in-memory data
     *
     * @param licensePlate Vehicle's license plate
     * @param dataOption   Data option of a vehicle
     * @return Value of the option (as Object)
     */
    public Object get(String licensePlate, Option dataOption) {
        Map<Option, Object> vehicleData = vehicleDataInMemory.get(licensePlate);
        if (vehicleData == null) {
            return null;
        }
        return vehicleData.get(dataOption);
    }

    /**
     * Set a data option of a vehicle in memory and queue database update
     *
     * @param licensePlate Vehicle's license plate
     * @param dataOption   Data option of a vehicle
     * @param value        New value of the option (should be the same type!)
     */
    public void set(String licensePlate, Option dataOption, Object value) {
        // Convert numbers to correct types before storing
        if (value != null) {
            try {
                switch (dataOption) {
                    case SKIN_DAMAGE:
                    case TRUNK_ROWS:
                        if (value instanceof Number) {
                            value = ((Number) value).intValue();
                        } else if (value instanceof String) {
                            value = Integer.parseInt((String) value);
                        } else {
                            Main.logSevere("Invalid type for " + dataOption + ": " + value.getClass().getName());
                            return;
                        }
                        break;

                    case ROTATION_SPEED:
                        if (value instanceof Number) {
                            value = ((Number) value).intValue();
                        } else if (value instanceof String) {
                            value = Integer.parseInt((String) value);
                        } else {
                            Main.logSevere("Invalid type for " + dataOption + ": " + value.getClass().getName());
                            return;
                        }
                        break;

                    case FUEL:
                    case FUEL_USAGE:
                    case BRAKING_SPEED:
                    case FRICTION_SPEED:
                    case ACCELERATION_SPEED:
                    case MAX_SPEED:
                    case MAX_SPEED_BACKWARDS:
                    case HEALTH:
                        if (value instanceof Number) {
                            value = ((Number) value).doubleValue();
                        } else if (value instanceof String) {
                            value = Double.parseDouble((String) value);
                        } else {
                            Main.logSevere("Invalid type for " + dataOption + ": " + value.getClass().getName());
                            return;
                        }
                        break;

                    case MEMBERS:
                    case RIDERS:
                        if (!(value instanceof List)) {
                            Main.logSevere("Invalid type for " + dataOption + ": Expected List, got " + value.getClass().getName());
                            return;
                        }
                        // Validate UUID strings
                        for (Object item : (List<?>) value) {
                            if (!(item instanceof String)) {
                                Main.logSevere("Invalid member UUID: " + item);
                                return;
                            }
                            try {
                                UUID.fromString((String) item);
                            } catch (IllegalArgumentException e) {
                                Main.logSevere("Invalid UUID format in " + dataOption + ": " + item);
                                return;
                            }
                        }
                        break;

                    case TRUNK_DATA:
                        if (!(value instanceof List)) {
                            Main.logSevere("Invalid type for " + dataOption + ": Expected List, got " + value.getClass().getName());
                            return;
                        }
                        // Validate all items are ItemStacks
                        for (Object item : (List<?>) value) {
                            if (item != null && !(item instanceof ItemStack)) {
                                Main.logSevere("Invalid item in trunk data: " + item.getClass().getName());
                                return;
                            }
                        }
                        break;
                }
            } catch (NumberFormatException e) {
                Main.logSevere("Invalid number format for " + dataOption + ": " + value + " (" + e.getMessage() + ")");
                return;
            }
        }

        Map<Option, Object> vehicleData = vehicleDataInMemory.computeIfAbsent(licensePlate, k -> new HashMap<>());
        vehicleData.put(dataOption, value);

        // Queue async save
        queueSaveOperation(() -> {
            try {
                // For basic vehicle data, we'll let the periodic save handle it
                // For complex data (members, riders, trunk), we need immediate updates

                if (dataOption == Option.MEMBERS || dataOption == Option.RIDERS) {
                    // Handle members/rider updates immediately
                    List<String> members = (List<String>) vehicleData.get(Option.MEMBERS);
                    List<String> riders = (List<String>) vehicleData.get(Option.RIDERS);

                    try (PreparedStatement deleteStmt = connection.prepareStatement(
                            "DELETE FROM vehicle_members WHERE license_plate = ?")) {
                        deleteStmt.setString(1, licensePlate);
                        deleteStmt.executeUpdate();
                    }

                    if (members != null && !members.isEmpty()) {
                        try (PreparedStatement insertStmt = connection.prepareStatement(
                                "INSERT INTO vehicle_members VALUES (?, ?, ?)")) {
                            for (String memberUuid : members) {
                                insertStmt.setString(1, licensePlate);
                                insertStmt.setString(2, memberUuid);
                                insertStmt.setBoolean(3, riders != null && riders.contains(memberUuid));
                                insertStmt.addBatch();
                            }
                            insertStmt.executeBatch();
                        }
                    }
                } else if (dataOption == Option.TRUNK_DATA) {
                    // Handle trunk updates immediately
                    List<String> trunkData = (List<String>) vehicleData.get(Option.TRUNK_DATA);

                    try (PreparedStatement deleteStmt = connection.prepareStatement(
                            "DELETE FROM vehicle_trunk WHERE license_plate = ?")) {
                        deleteStmt.setString(1, licensePlate);
                        deleteStmt.executeUpdate();
                    }

                    if (trunkData != null && !trunkData.isEmpty()) {
                        try (PreparedStatement insertStmt = connection.prepareStatement(
                                "INSERT INTO vehicle_trunk VALUES (?, ?, ?)")) {
                            for (int i = 0; i < trunkData.size(); i++) {
                                String itemData = trunkData.get(i);
                                if (itemData != null) {
                                    insertStmt.setString(1, licensePlate);
                                    insertStmt.setInt(2, i);
                                    insertStmt.setString(3, itemData);
                                    insertStmt.addBatch();
                                }
                            }
                            insertStmt.executeBatch();
                        }
                    }
                }
            } catch (SQLException e) {
                Main.logSevere("Failed to update vehicle data in database: " + e.getMessage());
                if (Main.instance.isEnabled()) {  // Only log stack trace if plugin is still enabled
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Delete a vehicle from in-memory data and database
     *
     * @param licensePlate Vehicle's license plate
     * @throws IllegalStateException If vehicle is already deleted.
     */
    public void delete(String licensePlate) throws IllegalStateException {
        if (!vehicleDataInMemory.containsKey(licensePlate)) {
            throw new IllegalStateException("An error occurred while trying to delete a vehicle. Vehicle is already deleted.");
        }

        vehicleDataInMemory.remove(licensePlate);

        // Queue async delete
        queueSaveOperation(() -> {
            try (PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM vehicles WHERE license_plate = ?")) {
                stmt.setString(1, licensePlate);
                stmt.executeUpdate();
            } catch (SQLException e) {
                Main.logSevere("Failed to delete vehicle from database: " + e.getMessage());
            }
        });
    }

    /**
     * Whether the vehicle database is empty
     */
    public boolean isEmpty() {
        return vehicleDataInMemory.isEmpty();
    }

    /**
     * Check whether 'hornEnabled' data option is set (it might not be if player was using an older version)
     * @param license Vehicle's license plate
     */
    public boolean isHornSet(String license) {
        return get(license, Option.HORN_ENABLED) != null;
    }
    /**
     * Check whether 'health' data option is set (it might not be if player was using an older version)
     * @param license Vehicle's license plate
     */
    public boolean isHealthSet(String license) {
        return get(license, Option.HEALTH) != null;
    }

    /**
     * Set the 'hornEnabled' data option default value.
     * @param license Vehicle's license plate
     */
    public void setInitialHorn(String license) {
        boolean state = VehicleUtils.getHornByDamage(getDamage(license));
        set(license, Option.HORN_ENABLED, state);
        saveToDatabase();
    }

    /**
     * Set the 'health' data option default value.
     * @param license Vehicle's license plate
     */
    public void setInitialHealth(String license) {
        double state = VehicleUtils.getMaxHealthByDamage(getDamage(license));
        set(license, Option.HEALTH, state);
        saveToDatabase();
    }

    /**
     * Get all vehicles data
     */
    public Map<String, Map<Option, Object>> getVehicles() {
        return new HashMap<>(vehicleDataInMemory);
    }

    /**
     * Get the durability of a vehicle item.
     * @param licensePlate Vehicle's license plate
     */
    public int getDamage(String licensePlate) {
        Integer damage = (Integer) get(licensePlate, Option.SKIN_DAMAGE);
        return damage != null ? damage : 0;
    }

    /**
     * Get the durability of a vehicle item.
     * @param vehicle Vehicle
     */
    public int getDamage(Vehicle vehicle) {
        return getDamage(vehicle.getLicensePlate());
    }

    /**
     * Get UUIDs of players which may sit in the vehicle
     * @param licensePlate Vehicle's license plate
     */
    public List<String> getMembers(String licensePlate) {
        Map<Option, Object> vehicleData = vehicleDataInMemory.get(licensePlate);
        if (vehicleData == null) return new ArrayList<>();

        List<String> members = (List<String>) vehicleData.get(Option.MEMBERS);
        return members != null ? new ArrayList<>(members) : new ArrayList<>();
    }

    /**
     * Get UUIDs of players which may steer the vehicle
     * @param licensePlate Vehicle's license plate
     */
    public List<String> getRiders(String licensePlate) {
        Map<Option, Object> vehicleData = vehicleDataInMemory.get(licensePlate);
        if (vehicleData == null) return new ArrayList<>();

        List<String> riders = (List<String>) vehicleData.get(Option.RIDERS);
        return riders != null ? new ArrayList<>(riders) : new ArrayList<>();
    }

    /**
     * Get data of the vehicle's trunk
     * @param licensePlate Vehicle's license plate
     * @return List of items in the trunk (as Strings)
     */
    public List<String> getTrunkData(String licensePlate) {
        Map<Option, Object> vehicleData = vehicleDataInMemory.get(licensePlate);
        if (vehicleData == null) return new ArrayList<>();

        List<String> trunkData = (List<String>) vehicleData.get(Option.TRUNK_DATA);
        return trunkData != null ? new ArrayList<>(trunkData) : new ArrayList<>();
    }

    /**
     * Get the type (enum) of the vehicle.
     * @param licensePlate Vehicle's license plate
     */
    public VehicleType getType(String licensePlate) {
        try {
            String typeStr = (String) get(licensePlate, Option.VEHICLE_TYPE);
            return typeStr != null ? VehicleType.valueOf(typeStr.toUpperCase(Locale.ROOT)) : VehicleType.CAR;
        } catch (IllegalArgumentException e) {
            Main.logSevere("An error occurred while setting a vehicle's type. Using default (CAR)...");
            return VehicleType.CAR;
        }
    }

    /**
     * Check whether horn may be used in a vehicle
     * @param license Vehicle's license plate
     */
    public boolean isHornEnabled(String license) {
        Boolean enabled = (Boolean) get(license, Option.HORN_ENABLED);
        if (enabled == null) {
            enabled = VehicleUtils.getHornByDamage(getDamage(license));
            set(license, Option.HORN_ENABLED, enabled);
            return enabled;
        }
        return enabled;
    }

    /**
     * Get health of a vehicle
     * @param license Vehicle's license plate
     */
    public double getHealth(String license) {
        Double health = (Double) get(license, Option.HEALTH);
        if (health == null) {
            health = VehicleUtils.getMaxHealthByDamage(getDamage(license));
            set(license, Option.HEALTH, health);
            return health;
        }
        return health;
    }

    /**
     * Damage a vehicle.
     * @param license Vehicle's license plate
     * @param damage Amount of damage
     */
    public void damageVehicle(String license, double damage) {
        double health = Math.max(0, getHealth(license) - damage);
        set(license, Option.HEALTH, health);
    }

    /**
     * Set health of a vehicle
     * @param license Vehicle's license plate
     * @param health New health
     */
    public void setHealth(String license, double health) {
        set(license, Option.HEALTH, health);
    }

    /**
     * Get number of vehicles owned by a player
     * @param p Player
     */
    public int getNumberOfOwnedVehicles(Player p) {
        String playerUUID = p.getUniqueId().toString();
        int count = 0;

        for (Map<Option, Object> vehicleData : vehicleDataInMemory.values()) {
            String owner = (String) vehicleData.get(Option.OWNER);
            if (playerUUID.equals(owner)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Close database connection when plugin disables
     */
    public void onDisable() {
        // Cancel auto-save task if running
        if (saveTask != null) {
            try {
                saveTask.cancel();
            } catch (Exception e) {
                Main.logWarning("Failed to cancel save task: " + e.getMessage());
            }
        }

        // Skip processing if already shutting down
        if (!Main.instance.isEnabled()) {
            Main.logInfo("Skipping final save - plugin is already disabling");
            return;
        }

        // Process any remaining save operations safely
        try {
            synchronized (saveQueue) {
                while (!saveQueue.isEmpty()) {
                    Runnable saveOperation = saveQueue.poll();
                    try {
                        if (saveOperation != null) {
                            saveOperation.run();
                        }
                    } catch (Exception e) {
                        Main.logWarning("Error executing save operation: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            Main.logWarning("Error processing save queue: " + e.getMessage());
        }

        // Close all user database connections
        for (Map.Entry<String, Connection> entry : userConnections.entrySet()) {
            try {
                Connection conn = entry.getValue();
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                    Main.logInfo("Closed database connection for user " + entry.getKey());
                }
            } catch (SQLException e) {
                Main.logSevere("Failed to close user database connection: " + e.getMessage());
            }
        }
        userConnections.clear();

        // Clear in-memory data
        vehicleDataInMemory.clear();
        saveQueue.clear();

        Main.logInfo("Vehicle data system shutdown complete");
    }

    /**
     * Options available in vehicle data
     */
    public enum Option {
        NAME("name"),
        VEHICLE_TYPE("vehicleType"),
        SKIN_ITEM("skinItem"),
        SKIN_DAMAGE("skinDamage"),
        OWNER("owner"),
        RIDERS("riders"),
        MEMBERS("members"),
        FUEL_ENABLED("fuelEnabled"),
        FUEL("fuel"),
        FUEL_USAGE("fuelUsage"),
        BRAKING_SPEED("brakingSpeed"),
        FRICTION_SPEED("frictionSpeed"),
        ACCELERATION_SPEED("accelerationSpeed"),
        MAX_SPEED("maxSpeed"),
        MAX_SPEED_BACKWARDS("maxSpeedBackwards"),
        ROTATION_SPEED("rotationSpeed"),
        TRUNK_ENABLED("trunkEnabled"),
        TRUNK_ROWS("trunkRows"),
        TRUNK_DATA("trunkData"),
        IS_OPEN("isOpen"),
        IS_GLOWING("isGlowing"),
        HORN_ENABLED("hornEnabled"),
        HEALTH("health"),
        NBT_VALUE("nbtValue"),
        IS_PUBLIC("isPublic");

        final private String path;

        Option(String path) {
            this.path = path;
        }

        /**
         * Get string path of option
         * @return Path of option
         */
        public String getPath() {
            return path;
        }
    }
}