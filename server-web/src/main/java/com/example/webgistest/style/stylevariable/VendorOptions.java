package com.example.webgistest.style.stylevariable;

/**
 * Enumeration of possible vendor options.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 */
public enum VendorOptions {
    VENDOROPTION_MAXDISPLACEMENT("maxDisplacement", "max displacement pixels"), // //$NON-NLS-1$
    VENDOROPTION_AUTOWRAP("autoWrap", "auto wrap pixels"), // //$NON-NLS-1$
    VENDOROPTION_SPACEAROUND("spaceAround", "space around pixels"), // //$NON-NLS-1$
    VENDOROPTION_REPEAT("repeat", "repeat every pixels"), // //$NON-NLS-1$
    VENDOROPTION_MAXANGLEDELTA("maxAngleDelta", "max angle allowed"), // //$NON-NLS-1$
    VENDOROPTION_FOLLOWLINE("followLine", "follow line"), // //$NON-NLS-1$
    VENDOROPTION_OTHER("other", "other"); //$NON-NLS-1$

    private String defString = null;
    private String guiString = null;

    VendorOptions(String defString, String guiString) {
        this.defString = defString;
        this.guiString = guiString;
    }

    /**
     * Return the vendoroption based on the definition string.
     *
     * @param defString the option definition string.
     * @return the {@link VendorOptions} or null.
     */
    public static VendorOptions toVendorOption(String defString) {
        VendorOptions[] values = values();
        for (VendorOptions vendorOptions : values) {
            if (defString.equals(vendorOptions.toString())) {
                return vendorOptions;
            }
        }
        return VendorOptions.VENDOROPTION_OTHER;
    }

    /**
     * Return the vendoroption based on the gui string.
     *
     * @param guiString the option gui string.
     * @return the {@link VendorOptions} or null.
     */
    public static VendorOptions guiStringToVendorOption(String guiString) {
        VendorOptions[] values = values();
        for (VendorOptions vendorOptions : values) {
            if (guiString.equals(vendorOptions.toGuiString())) {
                return vendorOptions;
            }
        }
        return null;
    }

    public String toString() {
        return defString;
    }

    public String toGuiString() {
        return guiString;
    }
}
