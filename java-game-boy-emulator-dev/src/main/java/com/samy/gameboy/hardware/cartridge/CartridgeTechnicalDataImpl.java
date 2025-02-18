package com.samy.gameboy.hardware.cartridge;

import java.util.HashMap;

/**
 * Created by samy badjoudj.
 */
public class CartridgeTechnicalDataImpl implements CartridgeTechnicalData{

    private static final HashMap<Integer, CartridgeTypeEnum> CARTRIDGE_TYPE = new HashMap<Integer, CartridgeTypeEnum>();
    private static final HashMap<Integer, Integer> BANKS_NUMBER = new HashMap<Integer, Integer>();
    private static final Integer BANK_OFFSET = 0x4000;



    private static final Integer DEFAULT_INDEX_CART_TYPE = 0x0147;
    private static final Integer DEFAULT_INDEX_NUMBER_BANKS = 0x0148;

    static {

        CARTRIDGE_TYPE.put(0x00, CartridgeTypeEnum.ROM);
        CARTRIDGE_TYPE.put(0x01, CartridgeTypeEnum.ROM_MBC1);
        CARTRIDGE_TYPE.put(0x02, CartridgeTypeEnum.ROM_MBC1_RAM);
        CARTRIDGE_TYPE.put(0x03, CartridgeTypeEnum.ROM_MBC1__RAM_BATT);
        CARTRIDGE_TYPE.put(0x04, CartridgeTypeEnum.UNKNOWN);
        CARTRIDGE_TYPE.put(0x05, CartridgeTypeEnum.ROM_MBC2);
        CARTRIDGE_TYPE.put(0x06, CartridgeTypeEnum.ROM_BC2_BATT);
        CARTRIDGE_TYPE.put(0x07, CartridgeTypeEnum.UNKNOWN);
        CARTRIDGE_TYPE.put(0x08, CartridgeTypeEnum.ROM_RAM);
        CARTRIDGE_TYPE.put(0x09, CartridgeTypeEnum.ROM_RAM_BATT);
        CARTRIDGE_TYPE.put(0x0A, CartridgeTypeEnum.UNKNOWN);
        CARTRIDGE_TYPE.put(0x0B, CartridgeTypeEnum.UNSUPPORTED_ROM_MMM1);
        CARTRIDGE_TYPE.put(0x0C, CartridgeTypeEnum.UNSUPPORTED_ROM_MMM1_SRAM);
        CARTRIDGE_TYPE.put(0x0D, CartridgeTypeEnum.UNSUPPORTED_ROM_MMM1_SRAM_BATT);
        CARTRIDGE_TYPE.put(0x0E,CartridgeTypeEnum.UNKNOWN);
        CARTRIDGE_TYPE.put(0x0F, CartridgeTypeEnum.ROM_MBC3_TIMER_BATT);
        CARTRIDGE_TYPE.put(0x10,CartridgeTypeEnum.ROM_MBC3_TIMER_RAM_BATT);
        CARTRIDGE_TYPE.put(0x11, CartridgeTypeEnum.ROM_MBC3);
        CARTRIDGE_TYPE.put(0x12, CartridgeTypeEnum.ROM_MBC3_RAM );
        CARTRIDGE_TYPE.put(0x13, CartridgeTypeEnum.ROM_MBC3_RAM_BATT);
        CARTRIDGE_TYPE.put(0x14, CartridgeTypeEnum.UNKNOWN);
        CARTRIDGE_TYPE.put(0x15, CartridgeTypeEnum.UNKNOWN);
        CARTRIDGE_TYPE.put(0x16, CartridgeTypeEnum.UNKNOWN);
        CARTRIDGE_TYPE.put(0x17, CartridgeTypeEnum.UNKNOWN);
        CARTRIDGE_TYPE.put(0x18, CartridgeTypeEnum.UNKNOWN);
        CARTRIDGE_TYPE.put(0x19, CartridgeTypeEnum.ROM_MBC5);
        CARTRIDGE_TYPE.put(0x1A, CartridgeTypeEnum.ROM_MBC5_RAM);
        CARTRIDGE_TYPE.put(0x1B, CartridgeTypeEnum.ROM_MBC5_RAM_BATT);
        CARTRIDGE_TYPE.put(0x1C, CartridgeTypeEnum.ROM_MBC5_RUMBLE);
        CARTRIDGE_TYPE.put(0x1D, CartridgeTypeEnum.ROM_MBC5_RUMBLE_RAM);
        CARTRIDGE_TYPE.put(0x1E, CartridgeTypeEnum.ROM_MBC5_RUMBLE_RAM_BATT);


        BANKS_NUMBER.put(0x00,2);
        BANKS_NUMBER.put(0x01,4);
        BANKS_NUMBER.put(0x02,8);
        BANKS_NUMBER.put(0x03,16);
        BANKS_NUMBER.put(0x04,32);
        BANKS_NUMBER.put(0x05,64);
        BANKS_NUMBER.put(0x06,128);
        BANKS_NUMBER.put(0x07,256);
        BANKS_NUMBER.put(0x52,72);
        BANKS_NUMBER.put(0x53,80);
        BANKS_NUMBER.put(0x54,96);



    }



    @Override
    public CartridgeTypeEnum getTypeOfCartridge(int numberType) {
        return CARTRIDGE_TYPE.get(numberType);
    }

    @Override
    public int getCartridgeNumberType(CartridgeTypeEnum cartridgeTypeEnum) {
        for (Integer next : CARTRIDGE_TYPE.keySet()) {
            final CartridgeTypeEnum typeEnum = CARTRIDGE_TYPE.get(next);
            if(typeEnum.equals(cartridgeTypeEnum))
                return next;
        }
        return 0;
    }

    @Override
    public int getNumberOfBanks(int numberType) {
        return BANKS_NUMBER.get(numberType);
    }

    @Override
    public int getBankOffset() {
        return BANK_OFFSET;
    }

    @Override
    public  int getDefaultIndexCartType() {
        return DEFAULT_INDEX_CART_TYPE;
    }

    @Override
    public int getDefaultIndexNumberBanks() {
        return DEFAULT_INDEX_NUMBER_BANKS;
    }

}
