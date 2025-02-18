package com.samy.gameboy.hardware.memory;

import com.samy.gameboy.cpu.GameBoyProcessor;
import com.samy.gameboy.hardware.cartridge.CartridgeInfos;
import com.samy.gameboy.hardware.cartridge.CartridgeTechnicalData;
import com.samy.gameboy.services.RomReader;
import org.apache.commons.io.IOUtils;

import java.io.IOException;


public class MemoryManagementUnitImpl implements MemoryManagementUnit {

    private final byte[] ramMemory = new byte[0x10000];
    private final int banksNumber;
    private final RomReader romReader;

    private final int[] bios = new int[]{
            0x31, 0xFE, 0xFF, 0xAF, 0x21, 0xFF, 0x9F, 0x32, 0xCB, 0x7C, 0x20, 0xFB, 0x21, 0x26, 0xFF, 0x0E,
            0x11, 0x3E, 0x80, 0x32, 0xE2, 0x0C, 0x3E, 0xF3, 0xE2, 0x32, 0x3E, 0x77, 0x77, 0x3E, 0xFC, 0xE0,
            0x47, 0x11, 0x04, 0x01, 0x21, 0x10, 0x80, 0x1A, 0xCD, 0x95, 0x00, 0xCD, 0x96, 0x00, 0x13, 0x7B,
            0xFE, 0x34, 0x20, 0xF3, 0x11, 0xD8, 0x00, 0x06, 0x08, 0x1A, 0x13, 0x22, 0x23, 0x05, 0x20, 0xF9,
            0x3E, 0x19, 0xEA, 0x10, 0x99, 0x21, 0x2F, 0x99, 0x0E, 0x0C, 0x3D, 0x28, 0x08, 0x32, 0x0D, 0x20,
            0xF9, 0x2E, 0x0F, 0x18, 0xF3, 0x67, 0x3E, 0x64, 0x57, 0xE0, 0x42, 0x3E, 0x91, 0xE0, 0x40, 0x04,
            0x1E, 0x02, 0x0E, 0x0C, 0xF0, 0x44, 0xFE, 0x90, 0x20, 0xFA, 0x0D, 0x20, 0xF7, 0x1D, 0x20, 0xF2,
            0x0E, 0x13, 0x24, 0x7C, 0x1E, 0x83, 0xFE, 0x62, 0x28, 0x06, 0x1E, 0xC1, 0xFE, 0x64, 0x20, 0x06,
            0x7B, 0xE2, 0x0C, 0x3E, 0x87, 0xF2, 0xF0, 0x42, 0x90, 0xE0, 0x42, 0x15, 0x20, 0xD2, 0x05, 0x20,
            0x4F, 0x16, 0x20, 0x18, 0xCB, 0x4F, 0x06, 0x04, 0xC5, 0xCB, 0x11, 0x17, 0xC1, 0xCB, 0x11, 0x17,
            0x05, 0x20, 0xF5, 0x22, 0x23, 0x22, 0x23, 0xC9, 0xCE, 0xED, 0x66, 0x66, 0xCC, 0x0D, 0x00, 0x0B,
            0x03, 0x73, 0x00, 0x83, 0x00, 0x0C, 0x00, 0x0D, 0x00, 0x08, 0x11, 0x1F, 0x88, 0x89, 0x00, 0x0E,
            0xDC, 0xCC, 0x6E, 0xE6, 0xDD, 0xDD, 0xD9, 0x99, 0xBB, 0xBB, 0x67, 0x63, 0x6E, 0x0E, 0xEC, 0xCC,
            0xDD, 0xDC, 0x99, 0x9F, 0xBB, 0xB9, 0x33, 0x3E, 0x3c, 0x42, 0xB9, 0xA5, 0xB9, 0xA5, 0x42, 0x4C,
            0x21, 0x04, 0x01, 0x11, 0xA8, 0x00, 0x1A, 0x13, 0xBE, 0x20, 0xFE, 0x23, 0x7D, 0xFE, 0x34, 0x20,
            0xF5, 0x06, 0x19, 0x78, 0x86, 0x23, 0x05, 0x20, 0xFB, 0x86, 0x20, 0xFE, 0x3E, 0x01, 0xE0, 0x50
    };

    private final CartridgeTechnicalData cartridgeTechnicalData;
    private GameBoyProcessor gameBoyProcessor;
    private int rombank,rambank,ramon;
    private boolean mode;
    private int romOffset= 0x4000;
    private int ie= 0;
    private int iflag= 0;
    private int inbios;
    private byte[] rom;
    private int[] wram = new int[8192];
    private int[] eram = new int[32768];
    private int[] zram = new int[127];
    


    private final CartridgeInfos cartrigeInfos;
    private int ramoffs;


    public MemoryManagementUnitImpl(String fileName, RomReader romReader, CartridgeTechnicalData technicalData, GameBoyProcessor gameBoyProcessor) throws IOException {
        this.cartridgeTechnicalData = technicalData;
        this.romReader = romReader;
        this.rom = IOUtils.toByteArray(this.romReader.getRom(fileName));
        this.cartrigeInfos = new CartridgeInfos(fileName,
                technicalData.getTypeOfCartridge(rom[cartridgeTechnicalData.getDefaultIndexCartType()]),
                technicalData.getCartridgeNumberType(technicalData.getTypeOfCartridge(rom[cartridgeTechnicalData.getDefaultIndexCartType()])));

        this.banksNumber = rom[cartridgeTechnicalData.getDefaultIndexNumberBanks()];
        this.inbios = 1;
        this.gameBoyProcessor = gameBoyProcessor;

    }

    @Override
    public int readWord(int addr) {
        switch (addr & 0xF000) {
            // ROM bank 0
            case 0x0000:
                if (inbios != 0) {
                    if (addr < 0x0100) {
                        return this.bios[addr] & 255;
                    } else if (gameBoyProcessor.getProgramCounter() == 0x0100) {
                        this.inbios = 0;
                    }
                } else {
                    return this.rom[addr];
                }

            case 0x1000:
            case 0x2000:
            case 0x3000:
                return this.rom[addr];
            // ROM bank 1
            case 0x4000:
            case 0x5000:
            case 0x6000:
            case 0x7000:
                return this.rom[this.romOffset + addr & 0x3FFF];

            // VRAM
            case 0x8000:
            case 0x9000:
                return 0;//GPU VRAM;

            // External RAM
            case 0xA000:
            case 0xB000:
                return this.eram[this.rom[this.romOffset + addr & 0x1FFF]];

            // Work RAM and echo
            case 0xC000:
            case 0xD000:
            case 0xE000:
                return this.wram[addr & 0x1FFF];

            // Everything else
            case 0xF000:
                switch (addr & 0x0F00) {
            // Echo RAM
                    case 0x000:
                    case 0x100:
                    case 0x200:
                    case 0x300:
                    case 0x400:
                    case 0x500:
                    case 0x600:
                    case 0x700:
                    case 0x800:
                    case 0x900:
                    case 0xA00:
                    case 0xB00:
                    case 0xC00:
                    case 0xD00:
                        return this.wram[addr & 0x1FFF];

                    // OAM
                    case 0xE00:
                        return ((addr & 0xFF) < 0xA0) ? -1: 0;//GPU OAM

                    // Zeropage RAM, I/O, interrupts
                    case 0xF00:
                        if (addr == 0xFFFF) {
                            return this.ie;
                        } else if (addr > 0xFF7F) {
                            return this.zram[addr & 0x7F];
                        } else switch (addr & 0xF0) {
                            case 0x00:
                                switch (addr & 0xF) {
                                    case 0:
                                        return 0; // JOYP
                                    case 4:
                                    case 5:
                                    case 6:
                                    case 7:
                                        return 0; //READ TIMER;
                                    case 15:
                                        return iflag; // Interrupt flags
                                    default:
                                        return 0;
                                }

                            case 0x10:
                            case 0x20:
                            case 0x30:
                                return 0;

                            case 0x40:
                            case 0x50:
                            case 0x60:
                            case 0x70:
                                return 0;//READ GPU;
                        }
                }

        }
        return 0;

    }


    @Override
    public int writeWord(int addr, int val) {
        switch(addr&0xF000)
        {
            // ROM bank 0
            // MBC1: Turn external RAM on
            case 0x0000: case 0x1000:
            switch(cartrigeInfos.getCartridgeTypeNumber())
            {
                case 1:
                    this.ramon = ((val&0xF)==0xA)?1:0;
                    break;
            }
            break;

            // MBC1: ROM bank switch
            case 0x2000: case 0x3000:
            switch(cartrigeInfos.getCartridgeTypeNumber())
            {
                case 1:
                    this.rombank &= 0x60;
                    val &= 0x1F;
                    if(val==0l) val=1;
                    this.rombank |= val;
                    this.romOffset = this.rombank * 0x4000;
                    break;
            }
            break;

            // ROM bank 1
            // MBC1: RAM bank switch
            case 0x4000: case 0x5000:
            switch(cartrigeInfos.getCartridgeTypeNumber())
            {
                case 1:
                    if(this.mode)
                    {
                        this.rambank = (val&3);
                        this.ramoffs = this.rambank * 0x2000;
                    }
                    else
                    {
                        this.rombank &= 0x1F;
                        this.rombank |= ((val&3)<<5);
                        this.romOffset = this.rombank * 0x4000;
                    }
            }
            break;

            case 0x6000: case 0x7000:
            switch(cartrigeInfos.getCartridgeTypeNumber())
            {
                case 1:
                    this.mode = val==1;
                    break;
            }
            break;

            // VRAM
            case 0x8000: case 0x9000:
           //VRAM
            break;

            // External RAM
            case 0xA000: case 0xB000:
            eram[ramoffs+(addr&0x1FFF)] = val&255;
            break;

            // Work RAM and echo
            case 0xC000: case 0xD000: case 0xE000:
            wram[addr&0x1FFF] = val;
            break;

            // Everything else
            case 0xF000:
                switch(addr&0x0F00)
                {
                    // Echo RAM
                    case 0x000: case 0x100: case 0x200: case 0x300:
                    case 0x400: case 0x500: case 0x600: case 0x700:
                    case 0x800: case 0x900: case 0xA00: case 0xB00:
                    case 0xC00: case 0xD00:
                    wram[addr&0x1FFF] = val;
                    break;

                    // OAM
                    case 0xE00:
                        if((addr&0xFF)<0xA0) {
                            //WRITE OAM
                        }
                        //UPDATE OAM
                        break;

                    // Zeropage RAM, I/O, interrupts
                    case 0xF00:
                        if(addr == 0xFFFF) { ie = val; }
                        else if(addr > 0xFF7F) { zram[addr&0x7F]=val; }
                        else switch(addr&0xF0)
                            {
                                case 0x00:
                                    switch(addr&0xF)
                                    {
                                        case 0: //GET KEY; break;
                                        case 4: case 5: case 6: case 7: //WRITE TIMER break;
                                        case 15: iflag = val; break;
                                    }
                                    break;

                                case 0x10: case 0x20: case 0x30:
                                break;

                                case 0x40: case 0x50: case 0x60: case 0x70:
                               //WRITE GRAPHIC
                                break;
                            }
                }
                break;
        }
        return 0;
    }

    @Override
    public int readDoubleWord(int addr) {
        return this.readWord(addr)+(this.readWord(addr+1)<<8);
    }

    @Override
    public void writeDoubleWord(int addr, int value) {
       this.writeWord(addr,value&255);
       this.writeWord(addr+1,value>>8);
    }

    @Override
    public void reset() {

    }

    @Override
    public void restart() {

    }

    @Override
    public void load() {

    }
}
