package com.samy.gameboy.hardware.memory;

/**
 * Created by samy badjoudj.
 */
public interface MemoryManagementUnit {

    public int readWord(int addr);
    public int writeWord(int addr, int value);

    public int readDoubleWord(int addr);
    public void writeDoubleWord(int addr, int value);

    public void reset();
    public void restart();

    public void load();
}
