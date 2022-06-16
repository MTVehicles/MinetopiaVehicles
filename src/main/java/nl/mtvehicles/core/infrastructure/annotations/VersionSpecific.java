package nl.mtvehicles.core.infrastructure.annotations;

/**
 * Warns that such an object/method uses a {@link nl.mtvehicles.core.infrastructure.enums.ServerVersion} getter.
 * <em>Usually, it must be edited when adding compatibility with a newer version (usually due to the usage of NMS).</em>
 *
 * @since 2.4.3
 */
public @interface VersionSpecific {}
