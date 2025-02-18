package com.samy.gameboy.hardware.cartridge;

/**
 * Created by samy badjoudj.
 */
public interface CartridgeTechnicalData {

    public CartridgeTypeEnum getTypeOfCartridge(int numberType);
    public int getCartridgeNumberType(CartridgeTypeEnum cartridgeTypeEnum);
    public int getNumberOfBanks(int numberType);
    public int getBankOffset();
    public int getDefaultIndexCartType();
    public int getDefaultIndexNumberBanks();


}
