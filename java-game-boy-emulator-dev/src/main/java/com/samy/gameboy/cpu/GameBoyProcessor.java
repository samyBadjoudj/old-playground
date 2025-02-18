package com.samy.gameboy.cpu;

import com.samy.gameboy.hardware.memory.MemoryManagementUnit;


public interface GameBoyProcessor {

    void setMemoryManagementUnit(MemoryManagementUnit  memoryManagementUnit);

    public int getRegisterA();

    public int getRegisterB() ;

    public int getRegisterC() ;

    public int getRegisterD();

    public int getRegisterE();

    public int getRegisterF();

    public int getRegisterH();

    public int getRegisterL();

    public int getStackPointer();

    public int getProgramCounter();

    public int getMachineCycle();

    public int getClockMachineCycle();

    public int getInterruptMode();

    public int getHalt() ;
}
