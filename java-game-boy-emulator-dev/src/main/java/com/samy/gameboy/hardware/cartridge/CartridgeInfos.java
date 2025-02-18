package com.samy.gameboy.hardware.cartridge;

/**
 * Created by samy badjoudj.
 */
public class CartridgeInfos {

    private final String romName;
    private final CartridgeTypeEnum  cartridgeType;
    private int  cartridgeTypeNumber;

    public CartridgeInfos(String romName, CartridgeTypeEnum cartridgeType, int cartridgeTypeNumber) {
        this.romName = romName;
        this.cartridgeType = cartridgeType;
        this.cartridgeTypeNumber = cartridgeTypeNumber ;
    }

    public CartridgeTypeEnum getCartridgeType() {
        return cartridgeType;
    }

    public int getCartridgeTypeNumber() {
        return cartridgeTypeNumber;
    }
}
