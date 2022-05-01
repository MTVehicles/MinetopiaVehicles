package nl.mtvehicles.core.events.interfaces;

import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;

public interface HasInventory {

    /**
     * Get the title of the inventory player has clicked in
     * @return Inventory title
     *
     * @see InventoryTitle#getStringTitle()
     */
    public InventoryTitle getTitle();

    /**
     * Set the new title of the inventory player has clicked in. This will be used by the plugin.
     * @param title New inventory title (enum!)
     *
     * @see InventoryTitle#getByStringTitle(String)
     */
    public void setTitle(InventoryTitle title);

}
