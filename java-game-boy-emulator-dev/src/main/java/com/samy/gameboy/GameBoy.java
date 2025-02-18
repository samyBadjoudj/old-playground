package com.samy.gameboy;

import com.samy.gameboy.cpu.GameBoyProcessor;
import com.samy.gameboy.cpu.Z80Processor;
import com.samy.gameboy.hardware.cartridge.CartridgeTechnicalDataImpl;
import com.samy.gameboy.hardware.memory.MemoryManagementUnit;
import com.samy.gameboy.hardware.memory.MemoryManagementUnitImpl;
import com.samy.gameboy.services.RomReaderImpl;

import java.io.IOException;

/**
 * Created by samy badjoudj
 */
public class GameBoy {


    private final GameBoyProcessor gameBoyProcessor;
    private final MemoryManagementUnit memoryManagementUnit;
    private final RomReaderImpl romReader = new RomReaderImpl();
    private final CartridgeTechnicalDataImpl cartridgeTechnicalData = new CartridgeTechnicalDataImpl();

    public GameBoy(String romFile) throws IOException {
        this.gameBoyProcessor = new Z80Processor();
        this.memoryManagementUnit = new MemoryManagementUnitImpl(romFile, romReader, cartridgeTechnicalData,gameBoyProcessor);



    }
}
