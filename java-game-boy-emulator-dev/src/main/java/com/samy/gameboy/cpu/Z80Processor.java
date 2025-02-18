package com.samy.gameboy.cpu;

import com.samy.gameboy.hardware.memory.MemoryManagementUnit;

import java.util.HashMap;

//TODO Split switch with bit masks
public class Z80Processor implements GameBoyProcessor{

    private MemoryManagementUnit memoryManagementUnit;

    private int registerA,
                registerB,
                registerC,
                registerD,
                registerE,
                registerF,
                registerH,
                registerL;

    private int registerAIndex=0,
                registerBIndex=1,
                registerCIndex=2,
                registerDIndex=3,
                registerEIndex=4,
                registerFIndex=5,
                registerHIndex=6,
                registerLIndex=7;

    private int registerASave,
                registerBSave,
                registerCSave,
                registerDSave,
                registerESave,
                registerFSave,
                registerHSave,
                registerLSave;

    private int allMainRegisters[] = { registerA,
                                registerB,
                                registerC,
                                registerD,
                                registerE,
                                registerF,
                                registerH,
                                registerL};
    private int registerR;
    private int registerI;

    private int stackPointer, programCounter;
    private int machineCycle, clockMachineCycle;
    private int interruptMode, halt;

    private final HashMap<String, Integer> CPU_REGISTERS = new HashMap<String, Integer>();


    public Z80Processor() {

    }

    public void saveState() {
    }

    public void restore() {

    }

    public void execute(int operationCode) {
        int newValue, lastBit, fifthBit, carryFlagBit, firstBit, hl, memoryValue = 0;
        switch (operationCode) {
            case 0x00://NOP
                this.machineCycle = 1;
            case 0x01://LDBCnn
                this.load2(registerBIndex, registerCIndex);
            case 0x02://LDBCmA
                this.memoryManagementUnit.writeWord((this.allMainRegisters[registerBIndex] << 8) + this.allMainRegisters[registerCIndex], this.allMainRegisters[registerAIndex]);
                this.machineCycle = 2;
            case 0x03://INCBC
                this.inc2(registerBIndex, registerCIndex);
            case 0x04://INCr_B
                this.inc(registerBIndex);
            case 0x05://DECr_B
                this.dec(registerBIndex);
            case 0x06://LDrn_b
                this.load(registerBIndex);
            case 0x07: //RLCA
                lastBit = ((registerA & 0x80) != 0) ? 1 : 0;
                this.allMainRegisters[registerAIndex] = this.allMainRegisters[registerAIndex] << 1;
                this.allMainRegisters[registerAIndex] = (this.allMainRegisters[registerAIndex] + lastBit) & 255;
                this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerFIndex] & 0xEF) + (lastBit == 1 ? 0x10 : 0);
                this.machineCycle = 1;
            case 0x08://LDmmSP
                this.memoryManagementUnit.writeDoubleWord(memoryManagementUnit.readDoubleWord(programCounter), stackPointer);
            case 0x09://ADDHLBC
                this.add2(registerHIndex, registerLIndex, registerBIndex, registerCIndex);
            case 0x0A://LDABCm
                this.allMainRegisters[registerAIndex] = memoryManagementUnit.readWord((this.allMainRegisters[registerBIndex] << 8) + this.allMainRegisters[registerCIndex]);
                this.machineCycle = 2;
            case 0x0B://DECBC
                this.dec2(registerBIndex, registerCIndex);
            case 0x0C://INCr_c
                this.inc(registerCIndex);
            case 0x0D://DECr_c
                this.dec(registerCIndex);
            case 0x0E://LDrn_c
                this.load(registerCIndex);
            case 0x0F://RRCA
                lastBit = (this.allMainRegisters[registerAIndex] & 1) != 0 ? 0x80 : 0;
                fifthBit = (this.allMainRegisters[registerAIndex] & 1) != 0 ? 0x10 : 0;
                this.allMainRegisters[registerAIndex] = ((this.allMainRegisters[registerAIndex] >> 1) + lastBit) & 255;
                //may check if is that's correct
                this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerFIndex] & 0xEF) + fifthBit;
                this.machineCycle = 1;
            case 0x10://DJNZn
                newValue = memoryManagementUnit.readWord(this.programCounter);
                if (newValue > 127) {
                    newValue = -((~newValue + 1) & 255);
                }
                this.programCounter++;
                this.machineCycle = 2;
                this.allMainRegisters[registerBIndex]--;
                if (registerB != 0) {
                    this.programCounter += newValue;
                    this.machineCycle++;
                }
            case 0x11://LDDEnn
                this.load2(registerDIndex, registerEIndex);
            case 0x12://LDDEmA
                this.memoryManagementUnit.writeWord((this.allMainRegisters[registerEIndex] << 8) + this.allMainRegisters[registerEIndex], this.allMainRegisters[registerAIndex]);
                this.machineCycle = 2;
            case 0x13://INCDE
                this.inc2(registerDIndex, registerEIndex);
            case 0x14://INCr_d
                this.inc(registerDIndex);
            case 0x15://DECr_d
                this.dec(registerDIndex);
            case 0x16://LDrn_d
                this.load(registerDIndex);
            case 0x17://RLA
                carryFlagBit = (this.allMainRegisters[registerFIndex] & 0x10) != 0 ? 1 : 0;
                firstBit = (this.allMainRegisters[registerAIndex] & 0x80) != 0 ? 0x10 : 0;
                this.allMainRegisters[registerAIndex] = (((this.allMainRegisters[registerAIndex] << 1) + carryFlagBit) & 255);
                this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerFIndex] & 0xEF) + firstBit;
                this.machineCycle = 1;
            case 0x18://JRn
                newValue = memoryManagementUnit.readWord(this.programCounter);
                //is that necessary ???
                if (newValue > 127) {
                    newValue = -((~newValue + 1) & 255);
                }
                this.programCounter++;
                this.machineCycle = 2;
                this.programCounter += newValue;
                this.machineCycle++;
            case 0x19://ADDHLDE
                this.add2(registerHIndex, registerLIndex, registerDIndex, registerEIndex);
            case 0x1A://LDADEm
                this.allMainRegisters[registerAIndex] = memoryManagementUnit.readWord((this.registerD << 8) + this.allMainRegisters[registerEIndex]);
                this.machineCycle = 2;
            case 0x1B://DECDE
                this.dec2(registerDIndex, registerEIndex);
            case 0x1C://INCrE
                this.inc(registerEIndex);
            case 0x1D://DECrE
                this.dec(registerEIndex);
            case 0x1E://LDrE
                this.load(registerEIndex);
            case 0x1F://RRA
                carryFlagBit = (this.allMainRegisters[registerFIndex] & 0x10) != 0 ? 0x80 : 0;
                firstBit = (this.allMainRegisters[registerAIndex] & 1) != 0 ? 0x10 : 0;
                this.allMainRegisters[registerAIndex] = ((this.allMainRegisters[registerAIndex] >> 1) + carryFlagBit) & 255;
                this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerFIndex] & 0xEF) + firstBit;
            case 0x20://JRNZn
                this.addJump(0x80, 0x00);
            case 0x21://LDHLnn
                this.load2(registerHIndex, registerLIndex);
            case 0x22://LDHLIA
                this.memoryManagementUnit.writeWord((this.allMainRegisters[registerHIndex] << 8) + this.allMainRegisters[registerLIndex], this.allMainRegisters[registerAIndex]);
                this.allMainRegisters[registerLIndex] = (this.allMainRegisters[registerLIndex] + 1) & 255;
                if (this.allMainRegisters[registerLIndex] == 0) {
                    this.allMainRegisters[registerHIndex] = (this.allMainRegisters[registerHIndex] + 1) & 255;
                }
                this.machineCycle = 2;
            case 0x23://INCHL
                this.inc2(registerHIndex, registerLIndex);
            case 0x24://INCH
                this.inc(registerHIndex);
            case 0x25://DECH
                this.dec(registerHIndex);        //var ci=Z80._r.b&0x80; var co=Z80._r.b&1?0x10:0; Z80._r.b=((Z80._r.b>>1)+ci)&255; Z80._r.f=(Z80._r.b)?0:0x80; Z80._r.f=(Z80._r.f&0xEF)+co; Z80._r.m=2;

            case 0x26://LDrnH
                this.load(registerHIndex);
            case 0x27://DAA
                newValue = this.allMainRegisters[registerAIndex];
                if (((this.allMainRegisters[registerFIndex] & 0x20) != 0) || ((this.allMainRegisters[registerAIndex] & 15) > 9)) {
                    this.allMainRegisters[registerAIndex] += 6;
                }
                this.allMainRegisters[registerFIndex] &= 0xEF;
                if (((this.allMainRegisters[registerFIndex] & 0x20) != 0) || newValue > 0x99) {
                    this.allMainRegisters[registerAIndex] += 0x60;
                    this.allMainRegisters[registerFIndex] |= 0x10;
                }
                this.machineCycle = 1;
            case 0x28://JRZn
                this.addJump(0x80, 0x80);
            case 0x29: //ADDHLHL
                this.add2(registerHIndex, registerLIndex, registerHIndex, registerLIndex);
            case 0x2A://LDAHLI
                this.allMainRegisters[registerAIndex] = (memoryManagementUnit.readWord((this.allMainRegisters[registerHIndex] << 8) + this.allMainRegisters[registerLIndex])) & 255;
                this.inc2(registerHIndex, registerLIndex);
                this.machineCycle = 2;
            case 0x2B://DECHL
                this.dec2(registerHIndex, registerLIndex);
            case 0x2C://incrL
                this.inc(registerLIndex);
            case 0x2D://decrL
                this.dec(registerLIndex);
            case 0x2E://LDrn_l
                this.load(registerLIndex);
            case 0x2F://CPL
                this.allMainRegisters[registerAIndex] ^= 255;
                //verify F ??
                this.allMainRegisters[registerFIndex] = this.allMainRegisters[registerAIndex] != 0 ? 0 : 0x80;
                this.machineCycle = 1;
            case 0x30://JRNC
                this.addJump(0x10, 0x00);
            case 0x31://LDSPm
                this.stackPointer = memoryManagementUnit.readDoubleWord(this.programCounter);
                this.programCounter += 2;
                this.machineCycle = 3;
            case 0x32://LDHLDA
                this.memoryManagementUnit.writeWord((this.allMainRegisters[registerHIndex] << 8) + this.allMainRegisters[registerLIndex], this.allMainRegisters[registerAIndex]);
                this.dec2(registerHIndex, registerLIndex);
                this.machineCycle = 2;
            case 0x33://INCSP
                this.stackPointer = (this.stackPointer + 1) & 65535;
                this.machineCycle = 1;
            case 0x34://INCHLm
                newValue = (memoryManagementUnit.readWord((this.allMainRegisters[registerHIndex] << 8) + this.allMainRegisters[registerLIndex]) + 1) & 255;
                memoryManagementUnit.writeWord(((this.allMainRegisters[registerHIndex] << 8) + this.allMainRegisters[registerLIndex]), newValue);
                this.updateFlagRegisterIncDec(newValue);
                this.machineCycle = 3;
            case 0x35://DECHLM
                newValue = (memoryManagementUnit.readWord((this.allMainRegisters[registerHIndex] << 8) + this.allMainRegisters[registerLIndex]) - 1) & 255;
                memoryManagementUnit.writeWord(((this.allMainRegisters[registerHIndex] << 8) + this.allMainRegisters[registerLIndex]), newValue);
                this.updateFlagRegisterIncDec(newValue);
                this.machineCycle = 3;
            case 0x36://LDHLMn
                memoryManagementUnit.writeWord(((this.allMainRegisters[registerHIndex] << 8) + this.allMainRegisters[registerLIndex]), this.programCounter);
                this.programCounter++;
                this.machineCycle = 3;
            case 0x37://SCF
                this.allMainRegisters[registerFIndex] |= 0x10;
                this.machineCycle = 1;
            case 0x38://JRCn
                this.addJump(0x10, 0x10);
            case 0x39://ADDHLSP
                hl = (this.allMainRegisters[registerHIndex] << 8) + this.allMainRegisters[registerLIndex];
                hl += this.stackPointer;
                if (hl > 65535) {
                    this.allMainRegisters[registerFIndex] |= 0x10;
                } else {
                    this.allMainRegisters[registerFIndex] &= 0xEF;
                }
            case 0x3A://LDHLD
                this.allMainRegisters[registerAIndex] = memoryManagementUnit.readWord((this.allMainRegisters[registerHIndex] << 8) + this.allMainRegisters[registerLIndex]);
                this.dec2(registerHIndex, registerLIndex);
                this.machineCycle = 2;
            case 0x3B://DECSP
                this.stackPointer = (this.stackPointer - 1) & 65535;
                this.machineCycle = 1;
            case 0x3C://INcrA
                this.inc(registerAIndex);
            case 0x3D://DECrA
                this.dec(registerAIndex);
            case 0x3E://LDRnA
                this.load(registerAIndex);
            case 0x3F://CCF
                fifthBit = (this.allMainRegisters[registerFIndex] & 0x10) != 0 ? 0 : 0x10;
                this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerFIndex] & 0xEF) + fifthBit;
                this.machineCycle = 1;
            case 0x40://LDrr_bb
                this.loadTo(registerBIndex, registerBIndex);
            case 0x41://LDrr_bc
                this.loadTo(registerBIndex, registerCIndex);
            case 0x42://LDrr_bd
                this.loadTo(registerBIndex, registerDIndex);
            case 0x43://LDrr_be
                this.loadTo(registerBIndex, registerEIndex);
            case 0x44://LDrr_bh
                this.loadTo(registerBIndex, registerHIndex);
            case 0x45://LDrr_bl
                this.loadTo(registerBIndex, registerLIndex);
            case 0x46://LDrHLm_b
                this.loadHL(registerBIndex);
            case 0x47://LDrr_ba
                this.loadTo(registerBIndex, registerAIndex);
            case 0x48://LDrr_cb
                this.loadTo(registerCIndex, registerBIndex);
            case 0x49://LDrr_cc
                this.loadTo(registerCIndex, registerCIndex);
            case 0x4A://LDrr_cd
                this.loadTo(registerCIndex, registerDIndex);
            case 0x4B://LDrr_ce
                this.loadTo(registerCIndex, registerEIndex);
            case 0x4C://LDrr_ch
                this.loadTo(registerCIndex, registerHIndex);
            case 0x4D://LDrr_cl
                this.loadTo(registerCIndex, registerLIndex);
            case 0x4E://LDrHLm_c
                this.loadHL(registerCIndex);
            case 0x4F://LDrr_ca
                this.loadTo(registerCIndex, registerAIndex);
            case 0x50://LDrr_db
                this.loadTo(registerDIndex, registerBIndex);
            case 0x51://LDrr_dc
                this.loadTo(registerDIndex, registerCIndex);
            case 0x52://LDrr_dd
                this.loadTo(registerDIndex, registerDIndex);
            case 0x53://LDrr_dd
                this.loadTo(registerDIndex, registerEIndex);
            case 0x54://LDrr_dh
                this.loadTo(registerDIndex, registerHIndex);
            case 0x55://LDrr_dl
                this.loadTo(registerDIndex, registerLIndex);
            case 0x56://LDrHLm_d
                this.loadHL(registerDIndex);
            case 0x57://LDrr_da
                this.loadTo(registerDIndex, registerAIndex);
            case 0x58://LDrr_eb
                this.loadTo(registerEIndex, registerBIndex);
            case 0x59://LDrr_eb
                this.loadTo(registerEIndex, registerCIndex);
            case 0x5A://LDrr_eb
                this.loadTo(registerEIndex, registerDIndex);
            case 0x5B://LDrr_ee
                this.loadTo(registerEIndex, registerEIndex);
            case 0x5C://LDrr_eh
                this.loadTo(registerEIndex, registerHIndex);
            case 0x5D://LDrr_eh
                this.loadTo(registerEIndex, registerLIndex);
            case 0x5E://LDrHLm_e
                this.loadHL(registerEIndex);
            case 0x5F://LDrr_ea
                this.loadTo(registerEIndex, registerAIndex);
            case 0x60://LDrr_hb
                this.loadTo(registerHIndex, registerBIndex);
            case 0x61://LDrr_hc
                this.loadTo(registerHIndex, registerCIndex);
            case 0x62://LDrr_hd
                this.loadTo(registerHIndex, registerDIndex);
            case 0x63://LDrr_he
                this.loadTo(registerHIndex, registerEIndex);
            case 0x64://LDrr_hh
                this.loadTo(registerHIndex, registerHIndex);
            case 0x65://LDrr_hl
                this.loadTo(registerHIndex, registerLIndex);
            case 0x66://LDrHLm_h
                this.loadHL(registerHIndex);
            case 0x67://LDrr_ha
                this.loadTo(registerHIndex, registerAIndex);
            case 0x68://LDrr_lb
                this.loadTo(registerLIndex, registerBIndex);
            case 0x69://LDrr_lc
                this.loadTo(registerLIndex, registerCIndex);
            case 0x6A://LDrr_ld
                this.loadTo(registerLIndex, registerDIndex);
            case 0x6B://LDrr_le
                this.loadTo(registerLIndex, registerEIndex);
            case 0x6C://LDrr_lH
                this.loadTo(registerLIndex, registerHIndex);
            case 0x6D://LDrr_ll
                this.loadTo(registerLIndex, registerLIndex);
            case 0x6E://LDrHLm_l
                this.loadHL(registerLIndex);
            case 0x6F://LDrr_la
                this.loadTo(registerLIndex, registerAIndex);
            case 0x70://LDHLmr_b
                this.loadHL(registerBIndex);
            case 0x71://LDHLmr_c
                this.loadHL(registerCIndex);
            case 0x72://LDHLmr_d
                this.loadHL(registerDIndex);
            case 0x73://LDHLmr_e
                this.loadHL(registerEIndex);
            case 0x74://LDHLmr_h
                this.loadHL(registerHIndex);
            case 0x75://LDHLmr_l
                this.loadHL(registerLIndex);
            case 0x76://HALT
                this.halt = 1;
                this.machineCycle = 1;
            case 0x77://LDHLmr_a
                this.loadHL(registerAIndex);
            case 0x78://LDrr_ab
                this.loadTo(registerAIndex, registerBIndex);
            case 0x79://LDrr_ac
                this.loadTo(registerAIndex, registerCIndex);
            case 0x7A://LDrr_ad
                this.loadTo(registerAIndex, registerDIndex);
            case 0x7B://LDrr_ae
                this.loadTo(registerAIndex, registerEIndex);
            case 0x7C://LDrr_ah
                this.loadTo(registerAIndex, registerHIndex);
            case 0x7D://LDrr_al
                this.loadTo(registerAIndex, registerLIndex);
            case 0x7E://LDrr_al
                this.loadHL(registerAIndex);
            case 0x7F://LDrr_aa
                this.loadTo(registerAIndex, registerAIndex);
            case 0x80://ADDr_b
                this.add(registerBIndex);
            case 0x81://ADDr_c
                this.add(registerCIndex);
            case 0x82://ADDr_d
                this.add(registerDIndex);
            case 0x83://ADDr_e
                this.add(registerEIndex);
            case 0x84://ADDr_h
                this.add(registerHIndex);
            case 0x85://ADDr_l
                this.add(registerLIndex);
            case 0x86://ADDHL
                newValue = this.allMainRegisters[registerAIndex];
                memoryValue = memoryManagementUnit.readWord(((this.allMainRegisters[registerHIndex] << 8) + this.allMainRegisters[registerLIndex]));
                //HC value
                this.allMainRegisters[registerAIndex] += memoryValue;
                this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerAIndex] > 255) ? 0x10 : 0;
                this.allMainRegisters[registerAIndex] &= 255;
                if (this.allMainRegisters[registerAIndex] == 0) {
                    this.allMainRegisters[registerFIndex] |= 0x80;
                }
                if (((this.allMainRegisters[registerAIndex] ^ newValue ^ memoryValue) & 0x10) != 0) {
                    this.allMainRegisters[registerFIndex] |= 0x20;
                }
                this.machineCycle = 2;
            case 0x87://ADDr_a
                this.add(registerAIndex);
            case 0x88://ADCr_b
                this.addc(registerBIndex);
            case 0x89://ADCr_c
                this.addc(registerCIndex);
            case 0x8A://ADCr_d
                this.addc(registerDIndex);
            case 0x8B://ADCr_e
                this.addc(registerEIndex);
            case 0x8C://ADCr_h
                this.addc(registerHIndex);
            case 0x8D://ADCr_l
                this.addc(registerLIndex);
            case 0x8E://ADCHL
                newValue = this.allMainRegisters[registerAIndex];
                memoryValue = memoryManagementUnit.readWord(((this.allMainRegisters[registerHIndex] << 8) + this.allMainRegisters[registerLIndex]));
                this.allMainRegisters[registerAIndex] += memoryValue;
                this.allMainRegisters[registerAIndex] += ((this.allMainRegisters[registerFIndex] & 0x10) != 0) ? 1 : 0;
                this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerAIndex] > 255) ? 0x10 : 0;
                this.allMainRegisters[registerAIndex] &= 255;
                if (this.allMainRegisters[registerAIndex] == 0) {
                    this.allMainRegisters[registerFIndex] |= 0x80;
                }
                if (((this.allMainRegisters[registerAIndex] ^ newValue ^ memoryValue) & 0x10) != 0) {
                    this.allMainRegisters[registerFIndex] |= 0x20;
                }
                this.machineCycle = 2;

            case 0x8F://ADCr_a
                this.addc(registerAIndex);
            case 0x90://SUBrb
                this.sub(registerBIndex);
            case 0x91://SUBrc
                this.sub(registerCIndex);
            case 0x92://SUBrd
                this.sub(registerDIndex);
            case 0x93://SUBre
                this.sub(registerEIndex);
            case 0x94://SUBrh
                this.sub(registerHIndex);
            case 0x95://SUBrl
                this.sub(registerLIndex);
            case 0x96://SUBHL
                newValue = this.allMainRegisters[registerAIndex];
                memoryValue = memoryManagementUnit.readWord(((this.allMainRegisters[registerHIndex] << 8) + this.allMainRegisters[registerLIndex]));
                //HC value
                this.allMainRegisters[registerAIndex] -= memoryValue;
                this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerAIndex] < 0) ? 0x50 : 0x40;
                this.allMainRegisters[registerAIndex] &= 255;
                if (this.allMainRegisters[registerAIndex] == 0) {
                    this.allMainRegisters[registerFIndex] |= 0x80;
                }
                if (((this.allMainRegisters[registerAIndex] ^ newValue ^ memoryValue) & 0x10) != 0) {
                    this.allMainRegisters[registerFIndex] |= 0x20;
                }
                this.machineCycle = 2;

            case 0x97://SUBa
                this.sub(registerCIndex);
            case 0x98://SUBCr_b
                this.subc(registerBIndex);
            case 0x99://SUBCr_c
                this.subc(registerCIndex);
            case 0x9A://SUBCr_d
                this.subc(registerDIndex);
            case 0x9B://SUBCr_e
                this.subc(registerEIndex);
            case 0x9C://SUBCr_h
                this.subc(registerHIndex);
            case 0x9D://SUBCr_l
                this.subc(registerLIndex);
            case 0x9E://SUBCHL
                newValue = this.allMainRegisters[registerAIndex];
                memoryValue = memoryManagementUnit.readWord(((this.allMainRegisters[registerHIndex] << 8) + this.allMainRegisters[registerLIndex]));
                this.allMainRegisters[registerAIndex] -= memoryValue;
                this.allMainRegisters[registerAIndex] -= ((this.allMainRegisters[registerFIndex] & 0x10) != 0) ? 1 : 0;
                this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerAIndex] < 0) ? 0x50 : 0x40;
                this.allMainRegisters[registerAIndex] &= 255;
                if (this.allMainRegisters[registerAIndex] == 0) {
                    this.allMainRegisters[registerFIndex] |= 0x80;
                }
                if (((this.allMainRegisters[registerAIndex] ^ newValue ^ memoryValue) & 0x10) != 0) {
                    this.allMainRegisters[registerFIndex] |= 0x20;
                }
                this.machineCycle = 2;
            case 0x9F://SUBCr_b
                this.subc(registerBIndex);
            case 0xA0://AND_b
                this.and(registerBIndex);
            case 0xA1://AND_c
                this.and(registerCIndex);
            case 0xA2://AND_d
                this.and(registerDIndex);
            case 0xA3://AND_e
                this.and(registerEIndex);
            case 0xA4://AND_h
                this.and(registerHIndex);
            case 0xA5://AND_l
                this.and(registerLIndex);
            case 0xA6://AND HL
                this.allMainRegisters[registerAIndex] &= memoryManagementUnit.readWord(((this.allMainRegisters[registerHIndex] << 8) + this.allMainRegisters[registerLIndex]));
                this.allMainRegisters[registerAIndex] &= 255;
                this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerAIndex] != 0) ? 0 : 0x80;
                this.machineCycle = 2;
            case 0xA7://AND_a
                this.and(registerAIndex);
            case 0xA8://XOR_b
                this.xor(registerBIndex);
            case 0xA9://XOR_c
                this.xor(registerCIndex);
            case 0xAA://XOR_d
                this.xor(registerDIndex);
            case 0xAB://XOR_e
                this.xor(registerEIndex);
            case 0xAC://XOR_h
                this.xor(registerHIndex);
            case 0xAD://XOR_l
                this.xor(registerLIndex);
            case 0xAE://XOR_HL
                this.allMainRegisters[registerAIndex] ^= memoryManagementUnit.readWord(((this.allMainRegisters[registerHIndex] << 8) + this.allMainRegisters[registerLIndex]));
                this.allMainRegisters[registerAIndex] &= 255;
                this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerAIndex] != 0) ? 0 : 0x80;
                this.machineCycle = 1;
            case 0xAF://XOR_a
                this.xor(registerAIndex);
            case 0xB0://ORr_b
                this.or(registerBIndex);
            case 0xB1://ORr_c
                this.or(registerCIndex);
            case 0xB2://ORr_d
                this.or(registerDIndex);
            case 0xB3://ORr_e
                this.or(registerEIndex);
            case 0xB4://ORr_h
                this.or(registerHIndex);
            case 0xB5://ORr_l
                this.or(registerLIndex);
            case 0xB6://ORr_HL
                this.allMainRegisters[registerAIndex] |= memoryManagementUnit.readWord(((this.allMainRegisters[registerHIndex] << 8) + this.allMainRegisters[registerLIndex]));
                this.allMainRegisters[registerAIndex] &= 255;
                this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerAIndex] != 0) ? 0 : 0x80;
                this.machineCycle = 2;
            case 0xB7://ORr_a
                this.or(registerAIndex);
            case 0xB8://CPr_b
                this.cp(registerBIndex);
            case 0xB9://CPr_c
                this.cp(registerCIndex);
            case 0xBA://CPr_d
                this.cp(registerAIndex);
            case 0xBB://CPr_E
                this.cp(registerEIndex);
            case 0xBC://CPr_h
                this.cp(registerEIndex);
            case 0xBD://CPr_l
                this.cp(registerEIndex);
            case 0xBE://CPr_HL
                newValue = this.allMainRegisters[registerAIndex];
                memoryValue = memoryManagementUnit.readWord(((this.allMainRegisters[registerHIndex] << 8) + this.allMainRegisters[registerLIndex]));
                newValue -= memoryValue;
                this.allMainRegisters[registerFIndex] = (newValue < 0) ? 0x50 : 0x40;
                if (newValue == 0) {
                    this.allMainRegisters[registerFIndex] |= 0x80;
                }
                if (((this.allMainRegisters[registerAIndex] ^ newValue ^ memoryValue) & 0x10) != 0) {
                    this.allMainRegisters[registerFIndex] |= 0x20;
                }
                this.machineCycle = 2;
            case 0xBF://CPr_a
                this.cp(registerAIndex);
            case 0xC0://RET_NZ
                this.returnTo(0x80, 0x00);
            case 0xC1://POPBC
                this.pop(registerBIndex, registerCIndex);
            case 0xC2://JPNZnn
                this.jump(0x80, 0x00);
            case 0xC3://JPnn
                this.programCounter = this.memoryManagementUnit.readDoubleWord(this.programCounter);
                this.machineCycle = 3;
            case 0xC4://CALLNZnn
                this.call(0x80, 0x00);
            case 0xC5://PUSHBC
                this.push(registerBIndex, registerCIndex);
            case 0xC6://ADDn
                newValue = this.allMainRegisters[registerAIndex];
                memoryValue = this.memoryManagementUnit.readWord(this.programCounter);
                this.allMainRegisters[registerAIndex] += memoryValue;
                this.programCounter++;
                this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerAIndex] > 255) ? 0x10 : 0;
                this.allMainRegisters[registerAIndex] = 255;
                if (this.allMainRegisters[registerAIndex] == 0) {
                    this.allMainRegisters[registerFIndex] |= 0x80;
                }
                if (((this.allMainRegisters[registerAIndex] ^ newValue ^ memoryValue) & 0x10) != 0) {
                    this.allMainRegisters[registerFIndex] |= 0x20;
                }
                this.machineCycle = 2;
            case 0xC7://RST00
                this.restart(0x00);
            case 0xC8://RETZ
                this.returnTo(0x80, 0x80);
            case 0xC9://RET
                this.programCounter = this.memoryManagementUnit.readDoubleWord(this.stackPointer);
                this.stackPointer += 2;
                this.machineCycle = 3;
            case 0xCA://JPZnn
                this.jump(0x80, 0x80);

            case 0xCB://CBMapping
                int newInstruction = this.memoryManagementUnit.readWord(this.programCounter);
                this.programCounter++;
                this.programCounter &= 65535;
                this.executeCB(newInstruction);
            case 0xCC://CallNZ
                this.call(0x80, 0x80);
            case 0xCD://Callnn
                this.stackPointer -= 2;
                this.memoryManagementUnit.writeDoubleWord(this.stackPointer, this.programCounter + 2);
                this.programCounter = this.memoryManagementUnit.readDoubleWord(this.programCounter);
                this.machineCycle = 5;
            case 0xCE://ADCn
                newValue = this.allMainRegisters[registerAIndex];
                memoryValue = memoryManagementUnit.readWord(this.programCounter);
                this.allMainRegisters[registerAIndex] += memoryValue;
                this.programCounter++;
                this.allMainRegisters[registerAIndex] += ((this.allMainRegisters[registerFIndex] & 0x10)) != 0 ? 1 : 0;
                this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerAIndex] > 255) ? 0x10 : 0;
                this.allMainRegisters[registerAIndex] &= 255;
                if (this.allMainRegisters[registerAIndex] == 0) {
                    this.allMainRegisters[registerFIndex] |= 0x80;
                }
                if (((this.allMainRegisters[registerAIndex] ^ memoryValue ^ newValue) & 0x10) != 0) {
                    this.allMainRegisters[registerFIndex] |= 0x20;
                }
                this.machineCycle = 2;
            case 0xCF://RST08
                this.restart(0x08);
            case 0xD0://RETNC
                this.returnTo(0x10, 0x00);
            case 0xD1://POPDE
                this.pop(registerDIndex, registerEIndex);
            case 0xD2://JPNC
                this.jump(0x10, 0x00);
            case 0xD3:
            case 0xDB:
            case 0xDD:
            case 0xE3:
            case 0xE4:
            case 0xEB:
            case 0xEC:
            case 0xED:
            case 0xF4:
            case 0xFC:
            case 0xFD:
                this.notImplemented(operationCode);
            case 0xD4://CALLNCnn
                this.call(0x10, 0x00);
            case 0xD5://PUSHDE
                this.push(registerDIndex, registerEIndex);
            case 0xD6://SUBn
                newValue = this.allMainRegisters[registerAIndex];
                memoryValue = memoryManagementUnit.readWord(this.programCounter);
                this.allMainRegisters[registerAIndex] -= memoryValue;
                this.programCounter++;
                this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerAIndex] < 0) ? 0x50 : 0x40;
                this.allMainRegisters[registerAIndex] &= 255;
                if (this.allMainRegisters[registerAIndex] == 0) {
                    this.allMainRegisters[registerFIndex] |= 0x80;
                }
                if (((this.allMainRegisters[registerAIndex] ^ memoryValue ^ newValue) & 0x10) != 0) {
                    this.allMainRegisters[registerFIndex] |= 0x20;
                }
                this.machineCycle = 2;
            case 0xD7://RST10
                this.restart(0x10);
            case 0xD8://RETC
                this.returnTo(0x10, 0x10);
            case 0xD9://RETI
                this.interruptMode = 1;
                this.restore();
                this.programCounter = this.memoryManagementUnit.readDoubleWord(this.stackPointer);
                this.stackPointer += 2;
                this.machineCycle = 3;
            case 0xDA://JPCnn
                this.jump(0x10, 0x10);
            case 0xDC://CALLcnn
                this.call(0x10, 0x10);
            case 0xDE://SUBCn
                newValue = this.allMainRegisters[registerAIndex];
                memoryValue = memoryManagementUnit.readWord(this.programCounter);
                this.allMainRegisters[registerAIndex] -= memoryValue;
                this.programCounter++;
                this.allMainRegisters[registerAIndex] -= ((this.allMainRegisters[registerFIndex] & 0x10) != 0) ? 1 : 0;
                this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerAIndex] < 0) ? 0x50 : 0x40;
                this.allMainRegisters[registerAIndex] &= 255;
                if (this.allMainRegisters[registerAIndex] == 0) {
                    this.allMainRegisters[registerFIndex] |= 0x80;
                }
                //if((Z80._r.a^m^a)&0x10) Z80._r.f|=0x20; Z80._r.m=2; },
                if ((this.allMainRegisters[registerAIndex] ^ memoryValue ^ newValue) != 0) {
                    this.allMainRegisters[registerFIndex] |= 0x20;

                }
                this.machineCycle = 2;
            case 0xDF://RST0x18
                this.restart(0x18);
            case 0xE0://LDIOn
                //MMU.wb(0xFF00+MMU.rb(Z80._r.pc),Z80._r.a); Z80._r.pc++; Z80._r.m=3;
                this.memoryManagementUnit.writeWord(0xFF00 + this.memoryManagementUnit.readWord(this.programCounter), this.allMainRegisters[registerAIndex]);
                this.programCounter++;
                this.machineCycle = 3;
            case 0xE1://POPHL
                this.pop(registerHIndex, registerLIndex);
            case 0xE2://LDIOCA
                this.memoryManagementUnit.writeWord(0xFF00 + this.allMainRegisters[registerCIndex], this.allMainRegisters[registerAIndex]);
                this.machineCycle = 2;
            case 0xE5://PUSHHL
                this.push(registerHIndex, registerLIndex);
            case 0xE6://ANDn
                this.allMainRegisters[registerAIndex] &= this.memoryManagementUnit.readWord(this.programCounter);
                this.programCounter++;
                this.allMainRegisters[registerAIndex] &= 255;
                this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerAIndex] != 0) ? 0 : 0x80;
                this.machineCycle = 2;
            case 0xE7:// RST20
                this.restart(0x20);
            case 0xE8://ADDSPn
                memoryValue = this.memoryManagementUnit.readWord(this.programCounter);
                if (memoryValue > 127) {
                    memoryValue = -((~memoryValue + 1) & 255);
                }
                this.programCounter++;
                this.stackPointer += memoryValue;
                this.machineCycle = 4;

            case 0xE9://JPHL
                this.programCounter = (this.allMainRegisters[registerHIndex] << 8) + this.allMainRegisters[registerLIndex];
                this.machineCycle = 1;
            case 0xEA://LDmmA
                this.memoryManagementUnit.writeWord(this.memoryManagementUnit.readDoubleWord(this.programCounter), this.allMainRegisters[registerAIndex]);
                this.machineCycle = 4;
            case 0xEE://xORm
                this.allMainRegisters[registerAIndex] ^= this.memoryManagementUnit.readWord(this.programCounter);
                this.machineCycle = 2;
            case 0xEF://RST28
                this.restart(0x28);
            case 0xF0://LDAIOn
                this.allMainRegisters[registerAIndex] = this.memoryManagementUnit.readWord(0xFF00 + this.memoryManagementUnit.readWord(this.programCounter));
                this.programCounter++;
                this.machineCycle = 3;
            case 0xF1://POPAF
                this.pop(registerAIndex, registerFIndex);
            case 0xF2://LDAIOC,
                //{ Z80._r.a=MMU.rb(0xFF00+Z80._r.c); Z80._r.m=2; },
                this.allMainRegisters[registerAIndex] = this.memoryManagementUnit.readWord(0xFF00 + this.allMainRegisters[registerCIndex]);
                this.machineCycle = 2;
            case 0xF3://DI
                //Z80._r.ime=0; Z80._r.m=1;
                this.interruptMode = 0;
                this.machineCycle = 1;
            case 0xF5://PUSHAF
                this.push(registerAIndex, registerFIndex);
            case 0xF6://ORn
                //{ Z80._r.a|=MMU.rb(Z80._r.pc); Z80._r.pc++; Z80._r.a&=255; Z80._r.f=Z80._r.a?0:0x80; Z80._r.m=2;
                this.allMainRegisters[registerAIndex] |= this.memoryManagementUnit.readWord(this.programCounter);
                this.programCounter++;
                this.allMainRegisters[registerAIndex] &= 255;
                this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerAIndex] != 0 ? 0x00 : 0x80);
                this.machineCycle = 2;
            case 0xF7://RST30
                this.restart(0x30);
            case 0xF8://LDHLSPn
                memoryValue = this.memoryManagementUnit.readWord(this.programCounter);
                if (memoryValue > 127) {
                    memoryValue = ~((memoryValue + 1) & 255);
                }
                this.programCounter++;
                memoryValue += this.stackPointer;
                this.allMainRegisters[registerHIndex] = (memoryValue >> 8) & 255;
                this.allMainRegisters[registerLIndex] = memoryValue & 255;
                this.machineCycle = 3;
            case 0xF9://LDSPHL
                this.stackPointer = ((this.allMainRegisters[registerHIndex] << 8) + this.allMainRegisters[registerLIndex]);
                this.machineCycle = 2;
            case 0xFA://LDAmm
                this.allMainRegisters[registerAIndex] = memoryManagementUnit.readWord(memoryManagementUnit.readDoubleWord(programCounter));
                this.programCounter += 2;
                this.machineCycle = 4;
            case 0xFB://EI
                // Z80._r.ime=1; Z80._r.m=1;
                this.interruptMode = 1;
                this.machineCycle = 1;
            case 0xFE://CPn
                newValue = this.allMainRegisters[registerAIndex];
                memoryValue = this.memoryManagementUnit.readWord(this.programCounter);
                newValue -= memoryValue;
                this.programCounter++;
                this.allMainRegisters[registerFIndex] = (newValue < 0) ? 0x50 : 0x40;
                newValue &= 255;
                if (newValue == 0) {
                    this.allMainRegisters[registerFIndex] |= 0x80;

                }
                if (((this.allMainRegisters[registerAIndex] ^ newValue ^ memoryValue) & 0x10) != 0) {
                    this.allMainRegisters[registerFIndex] |= 0x20;
                }
                this.machineCycle = 2;
            case 0xFF://RST38
                this.restart(0x38);


        }

    }

    private void executeCB(int newInstruction) {
        int hl, lastBit, fifthBit, firstBit;
        switch (newInstruction) {
            case 0xCB00://RLCr_b
                this.rotateLeftCarry(registerBIndex);
            case 0xCB01://RLCr_c
                this.rotateLeftCarry(registerCIndex);
            case 0xCB02://RLCr_d
                this.rotateLeftCarry(registerDIndex);
            case 0xCB03://RLCr_e
                this.rotateLeftCarry(registerEIndex);
            case 0xCB04://RLCr_h
                this.rotateLeftCarry(registerHIndex);
            case 0xCB05://RLCr_l
                this.rotateLeftCarry(registerLIndex);
            case 0xCB06://RLCHL
                hl = this.memoryManagementUnit.readWord((this.allMainRegisters[registerHIndex] << 8) + this.allMainRegisters[registerLIndex]);
                lastBit = (hl & 0x80) != 0 ? 1 : 0;
                fifthBit = (hl & 0x80) != 0 ? 0x10 : 0;
                hl = (hl << 1) + lastBit;
                hl &= 255;
                this.allMainRegisters[registerFIndex] = (hl != 0) ? 0 : 0x80;
                this.memoryManagementUnit.writeWord((this.allMainRegisters[registerHIndex] << 8) + this.allMainRegisters[registerLIndex], hl);
                this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerFIndex] & 0xEF) + fifthBit;
                this.machineCycle = 4;

            case 0xCB07://RLCr_a
                this.rotateLeftCarry(registerAIndex);
            case 0xCB08://RRCr_b
                this.rotateRightCarry(registerBIndex);
            case 0xCB09://RRCr_b
                this.rotateRightCarry(registerCIndex);
            case 0xCB0A://RRCr_c
                this.rotateRightCarry(registerDIndex);
            case 0xCB0B://RRCr_d
                this.rotateRightCarry(registerEIndex);
            case 0xCB0C://RRCr_e
                this.rotateRightCarry(registerHIndex);
            case 0xCB0D://RRCr_h
                this.rotateRightCarry(registerLIndex);
            case 0xCB0E://RRCr_hl
                hl = this.memoryManagementUnit.readWord((this.allMainRegisters[registerHIndex] << 8) + this.allMainRegisters[registerLIndex]);
                lastBit = ((hl & 0x1) != 0) ? 0x80 : 0;
                fifthBit = ((hl & 0x1) != 0) ? 0x10 : 0;
                hl = (hl >> 1) + lastBit;
                hl &= 255;
                this.memoryManagementUnit.writeWord((this.allMainRegisters[registerHIndex] << 8) + this.allMainRegisters[registerLIndex], hl);
                this.allMainRegisters[registerFIndex] = (hl != 0) ? 0 : 0x80;
                this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerFIndex] & 0xEF) + fifthBit;
                this.machineCycle = 4;
            case 0xCB0F://RRCr_l
                this.rotateRightCarry(registerAIndex);
            case 0xCB10://RLr_B
                this.rotateLeft(registerBIndex);
            case 0xCB11://RLr_c
                this.rotateLeft(registerCIndex);
            case 0xCB12://RLr_d
                this.rotateLeft(registerDIndex);
            case 0xCB13://RLr_e
                this.rotateLeft(registerEIndex);
            case 0xCB14://RLrh
                this.rotateLeft(registerHIndex);
            case 0xCB15://RLrl
                this.rotateLeft(registerLIndex);
            case 0xCB16://RLHL
                hl = this.memoryManagementUnit.readWord((this.allMainRegisters[registerHIndex] << 8) + this.allMainRegisters[registerLIndex]);
                fifthBit = (this.allMainRegisters[registerFIndex] & 0x10) != 0 ? 1 : 0;
                lastBit = (hl & 0x80) != 0 ? 0x10 : 0;
                hl = (registerB << 1) + fifthBit;
                hl &= 255;
                memoryManagementUnit.writeWord((this.allMainRegisters[registerHIndex] << 8) + this.allMainRegisters[registerLIndex], hl);
                this.allMainRegisters[registerFIndex] = (hl != 0) ? 0 : 0x80;
                this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerFIndex] & 0xEF) + lastBit;
                this.machineCycle = 4;
            case 0xCB17://RLa
                this.rotateLeft(registerLIndex);
            case 0xCB18://RRb
                this.rotateRight(registerBIndex);
            case 0xCB19://RRc
                this.rotateRight(registerCIndex);
            case 0xCB1A://RRd
                this.rotateRight(registerDIndex);
            case 0xCB1B://RRe
                this.rotateRight(registerEIndex);
            case 0xCB1C://RRh
                this.rotateRight(registerHIndex);
            case 0xCB1D://RRl
                this.rotateRight(registerLIndex);
            case 0xCB1E://RRhl
                hl = this.memoryManagementUnit.readWord((this.allMainRegisters[registerHIndex] << 8) + this.allMainRegisters[registerLIndex]);
                fifthBit = (this.allMainRegisters[registerFIndex] & 0x10) != 0 ? 0x80 : 0;
                firstBit = ((hl & 1) != 0) ? 0x10 : 0;
                hl = (hl >> 1) + fifthBit;
                hl &= 255;
                memoryManagementUnit.writeWord((this.allMainRegisters[registerHIndex] << 8) + this.allMainRegisters[registerLIndex], hl);
                this.allMainRegisters[registerFIndex] = (hl != 0) ? 0 : 0x80;
                this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerFIndex] & 0xEF) + firstBit;
                this.machineCycle = 2;
            case 0xCB1F://RRa
                this.rotateRight(registerAIndex);
            case 0xCB20://SLAr_b
                this.shiftLeftCarry(registerBIndex);
            case 0xCB21://SLAr_c
                this.shiftLeftCarry(registerCIndex);
            case 0xCB22://SLAr_d
                this.shiftLeftCarry(registerDIndex);
            case 0xCB23://SLAr_e
                this.shiftLeftCarry(registerEIndex);
            case 0xCB24://SLAr_h
                this.shiftLeftCarry(registerHIndex);
            case 0xCB25://SLAr_l
                this.shiftLeftCarry(registerLIndex);
            case 0xCB26://SLAHL
                this.notImplemented(newInstruction);
            case 0xCB27://SLAr_a
                this.shiftLeftCarry(registerAIndex);
            case 0xCB28://SRAr_b
                this.shiftRightCarry(registerBIndex);
            case 0xCB29://SRAr_c
                this.shiftRightCarry(registerCIndex);
            case 0xCB2A://SRAr_d
                this.shiftRightCarry(registerDIndex);
            case 0xCB2B://SRAr_e
                this.shiftRightCarry(registerEIndex);
            case 0xCB2C://SRAr_h
                this.shiftRightCarry(registerHIndex);
            case 0xCB2D://SRAr_l
                this.shiftRightCarry(registerLIndex);
            case 0xCB2E://SRAr_hl
                this.notImplemented(newInstruction);
            case 0xCB2F://SRAr_a
                this.shiftRightCarry(registerAIndex);
            case 0xCB30://SWAPr_b
                this.swap(registerBIndex);
            case 0xCB31://SWAPr_c
                this.swap(registerCIndex);
            case 0xCB32://SWAPr_d
                this.swap(registerDIndex);
            case 0xCB33://SWAPr_e
                this.swap(registerEIndex);
            case 0xCB34://SWAPr_h
                this.swap(registerHIndex);
            case 0xCB35://SWAPr_l
                this.swap(registerLIndex);
            case 0xCB36://SWAPr_hl
                this.notImplemented(newInstruction);
            case 0xCB37://SWAPr_a
                this.swap(registerAIndex);
            case 0xCB38://SRL_b
                this.shiftRightCarryMSB(registerBIndex);
            case 0xCB39://SRL_c
                this.shiftRightCarryMSB(registerCIndex);
            case 0xCB3A://SRL_d
                this.shiftRightCarryMSB(registerDIndex);
            case 0xCB3B://SRL_e
                this.shiftRightCarryMSB(registerEIndex);
            case 0xCB3C://SRL_h
                this.shiftRightCarryMSB(registerHIndex);
            case 0xCB3D://SRL_l
                this.shiftRightCarryMSB(registerLIndex);
            case 0xCB3E://SRL_hl
                this.notImplemented(newInstruction);
            case 0xCB3F://SRL_a
                this.shiftRightCarryMSB(registerAIndex);
            case 0xCB40://BIT0_b
                this.setBit(registerBIndex, 0x01);
            case 0xCB41://BIT0_c
                this.setBit(registerCIndex, 0x01);
            case 0xCB42://BIT0_d
                this.setBit(registerDIndex, 0x01);
            case 0xCB43://BIT0_e
                this.setBit(registerEIndex, 0x01);
            case 0xCB44://BIT0_h
                this.setBit(registerHIndex, 0x01);
            case 0xCB45://BIT0_l
                this.setBit(registerLIndex, 0x01);
            case 0xCB46://BIT0_m
                this.setBitHL(0x01);
            case 0xCB47://BIT1_b
                this.setBit(registerAIndex, 0x01);
            case 0xCB48://BIT1_b
                this.setBit(registerBIndex, 0x02);
            case 0xCB49://BIT1_c
                this.setBit(registerCIndex, 0x02);
            case 0xCB4A://BIT1_d
                this.setBit(registerDIndex, 0x02);
            case 0xCB4B://BIT1_e
                this.setBit(registerEIndex, 0x02);
            case 0xCB4C://BIT1_h
                this.setBit(registerHIndex, 0x02);
            case 0xCB4D://BIT1_l
                this.setBit(registerLIndex, 0x02);
            case 0xCB4E://BIT1_m
                this.setBitHL(0x02);
            case 0xCB4F://BIT1_a
                this.setBit(registerAIndex, 0x02);
            case 0xCB50://BIT2_b
                this.setBit(registerBIndex, 0x04);
            case 0xCB51://BIT2_c
                this.setBit(registerCIndex, 0x04);
            case 0xCB52://BIT2_b
                this.setBit(registerDIndex, 0x04);
            case 0xCB53://BIT2_d
                this.setBit(registerEIndex, 0x04);
            case 0xCB54://BIT2_h
                this.setBit(registerHIndex, 0x04);
            case 0xCB55://BIT2_l
                this.setBit(registerLIndex, 0x04);
            case 0xCB56://BIT2_m
                this.setBitHL(0x04);
            case 0xCB57://BIT2_a
                this.setBit(registerAIndex, 0x04);
            case 0xCB58://BIT3_b
                this.setBit(registerBIndex, 0x08);
            case 0xCB59://BIT3_c
                this.setBit(registerCIndex, 0x08);
            case 0xCB5A://BIT3_d
                this.setBit(registerDIndex, 0x08);
            case 0xCB5B://BIT3_E
                this.setBit(registerEIndex, 0x08);
            case 0xCB5C://BIT3_h
                this.setBit(registerHIndex, 0x08);
            case 0xCB5D://BIT3_l
                this.setBit(registerLIndex, 0x08);
            case 0xCB5E://BIT3_hl
                this.setBitHL(0x08);
            case 0xCB5F://BIT3_A
                this.setBit(registerAIndex, 0x08);


            case 0xCB60://BIT4_b
                this.setBit(registerBIndex, 0x10);
            case 0xCB61://BIT4_c
                this.setBit(registerCIndex, 0x10);
            case 0xCB62://BIT4_d
                this.setBit(registerDIndex, 0x10);
            case 0xCB63://BIT4_e
                this.setBit(registerEIndex, 0x10);
            case 0xCB64://BIT4_h
                this.setBit(registerHIndex, 0x10);
            case 0xCB65://BIT4_l
                this.setBit(registerLIndex, 0x10);
            case 0xCB66://BIT4_m
                this.setBitHL(0x10);
            case 0xCB67://BIT4_a
                this.setBit(registerAIndex, 0x10);

            case 0xCB68://BIT5_b
                this.setBit(registerBIndex, 0x20);
            case 0xCB69://BIT5_c
                this.setBit(registerCIndex, 0x20);
            case 0xCB6A://BIT5_d
                this.setBit(registerDIndex, 0x20);
            case 0xCB6B://BIT5_e
                this.setBit(registerEIndex, 0x20);
            case 0xCB6C://BIT5_h
                this.setBit(registerHIndex, 0x20);
            case 0xCB6D://BIT5_l
                this.setBit(registerLIndex, 0x20);
            case 0xCB6E://BIT5_l
                this.setBitHL(0x20);
            case 0xCB6F://BIT5_a
                this.setBit(registerAIndex, 0x20);

            case 0xCB70://BIT6_b
                this.setBit(registerBIndex, 0x40);
            case 0xCB71://BIT6_c
                this.setBit(registerCIndex, 0x40);
            case 0xCB72://BIT6_d
                this.setBit(registerDIndex, 0x40);
            case 0xCB73://BIT6_e
                this.setBit(registerEIndex, 0x40);
            case 0xCB74://BIT6_h
                this.setBit(registerHIndex, 0x40);
            case 0xCB75://BIT6_l
                this.setBit(registerLIndex, 0x40);
            case 0xCB76://BIT6_m
                this.setBitHL(0x40);
            case 0xCB77://BIT6_a
                this.setBit(registerAIndex, 0x40);

            case 0xCB78://BIT7_b
                this.setBit(registerBIndex, 0x80);
            case 0xCB79://BIT7_c
                this.setBit(registerCIndex, 0x80);
            case 0xCB7A://BIT7_d
                this.setBit(registerDIndex, 0x80);
            case 0xCB7B://BIT7_e
                this.setBit(registerEIndex, 0x80);
            case 0xCB7C://BIT7_h
                this.setBit(registerHIndex, 0x80);
            case 0xCB7D://BIT7_l
                this.setBit(registerLIndex, 0x80);
            case 0xCB7E://BIT7_l
                this.setBitHL(0x80);
            case 0xCB7F://BIT7_a
                this.setBit(registerAIndex, 0x80);

            case 0xCB80://RES0b
                this.resetBit(registerBIndex, 0xEF);
            case 0xCB81://RES0C
                this.resetBit(registerCIndex, 0xEF);
            case 0xCB82://RES0d
                this.resetBit(registerDIndex, 0xEF);
            case 0xCB83://RES0e
                this.resetBit(registerEIndex, 0xEF);
            case 0xCB84://RES0h
                this.resetBit(registerHIndex, 0xEF);
            case 0xCB85://RES0l
                this.resetBit(registerLIndex, 0xEF);
            case 0xCB86://RES0m
                this.resetBitHL(0xEF);
            case 0xCB87://RES0l
                this.resetBit(registerAIndex, 0xEF);

            case 0xCB88://RES1b
                this.resetBit(registerBIndex, 0xFD);
            case 0xCB89://RES1c
                this.resetBit(registerCIndex, 0xFD);
            case 0xCB8A://RES1d
                this.resetBit(registerDIndex, 0xFD);
            case 0xCB8B://RES1e
                this.resetBit(registerEIndex, 0xFD);
            case 0xCB8C://RES1h
                this.resetBit(registerHIndex, 0xFD);
            case 0xCB8D://RES1l
                this.resetBit(registerLIndex, 0xFD);
            case 0xCB8E://RES1hl
                this.resetBitHL(0xFD);
            case 0xCB8F://RES1a
                this.resetBit(registerAIndex, 0xFD);

            case 0xCB90://RES2b
                this.resetBit(registerBIndex, 0xFB);
            case 0xCB91://RES2C
                this.resetBit(registerCIndex, 0xFB);
            case 0xCB92://RES2d
                this.resetBit(registerDIndex, 0xFB);
            case 0xCB93://RES2e
                this.resetBit(registerEIndex, 0xFB);
            case 0xCB94://RES2h
                this.resetBit(registerHIndex, 0xFB);
            case 0xCB95://RES2l
                this.resetBit(registerLIndex, 0xFB);
            case 0xCB96://RES2m
                this.resetBitHL(0xFB);
            case 0xCB97://RES2l
                this.resetBit(registerAIndex, 0xFB);

            case 0xCB98://RES3b
                this.resetBit(registerBIndex, 0xF7);
            case 0xCB99://RES3c
                this.resetBit(registerCIndex, 0xF7);
            case 0xCB9A://RES3d
                this.resetBit(registerDIndex, 0xF7);
            case 0xCB9B://RES3e
                this.resetBit(registerEIndex, 0xF7);
            case 0xCB9C://RES3h
                this.resetBit(registerHIndex, 0xF7);
            case 0xCB9D://RES3l
                this.resetBit(registerLIndex, 0xF7);
            case 0xCB9E://RES3hl
                this.resetBitHL(0xF7);
            case 0xCB9F://RES3a
                this.resetBit(registerAIndex, 0xF7);


            case 0xCBA0://RES4b
                this.resetBit(registerBIndex, 0xEF);
            case 0xCBA1://RES4C
                this.resetBit(registerCIndex, 0xEF);
            case 0xCBA2://RES4d
                this.resetBit(registerDIndex, 0xEF);
            case 0xCBA3://RES4e
                this.resetBit(registerEIndex, 0xEF);
            case 0xCBA4://RES4h
                this.resetBit(registerHIndex, 0xEF);
            case 0xCBA5://RES4l
                this.resetBit(registerLIndex, 0xEF);
            case 0xCBA6://RES4m
                this.resetBitHL(0xEF);
            case 0xCBA7://RES4l
                this.resetBit(registerAIndex, 0xEF);

            case 0xCBA8://RES5b
                this.resetBit(registerBIndex, 0xDF);
            case 0xCBA9://RES5c
                this.resetBit(registerCIndex, 0xDF);
            case 0xCBAA://RES5d
                this.resetBit(registerDIndex, 0xDF);
            case 0xCBAB://RES5e
                this.resetBit(registerEIndex, 0xDF);
            case 0xCBAC://RES5h
                this.resetBit(registerHIndex, 0xDF);
            case 0xCBAD://RES5l
                this.resetBit(registerLIndex, 0xDF);
            case 0xCBAE://RES5hl
                this.resetBitHL(0xDF);
            case 0xCBAF://RES5a
                this.resetBit(registerAIndex, 0xDF);

            case 0xCBB0://RES6b
                this.resetBit(registerBIndex, 0xBF);
            case 0xCBB1://RES6C
                this.resetBit(registerCIndex, 0xBF);
            case 0xCBB2://RES6d
                this.resetBit(registerDIndex, 0xBF);
            case 0xCBB3://RES6e
                this.resetBit(registerEIndex, 0xBF);
            case 0xCBB4://RES6h
                this.resetBit(registerHIndex, 0xBF);
            case 0xCBB5://RES6l
                this.resetBit(registerLIndex, 0xBF);
            case 0xCBB6://RES6m
                this.resetBitHL(0xBF);
            case 0xCBB7://RES6l
                this.resetBit(registerAIndex, 0xBF);

            case 0xCBB8://RES7b
                this.resetBit(registerBIndex, 0x7F);
            case 0xCBB9://RES7c
                this.resetBit(registerCIndex, 0x7F);
            case 0xCBBA://RES7d
                this.resetBit(registerDIndex, 0x7F);
            case 0xCBBB://RES7e
                this.resetBit(registerEIndex, 0x7F);
            case 0xCBBC://RES7h
                this.resetBit(registerHIndex, 0x7F);
            case 0xCBBD://RES7l
                this.resetBit(registerLIndex, 0x7F);
            case 0xCBBE://RES7hl
                this.resetBitHL(0x7F);
            case 0xCBBF://RES7a
                this.resetBit(registerAIndex, 0x7F);


            case 0xCBC0://SET0b
                this.set(registerBIndex,0x01);
            case 0xCBC1://SET0c
                this.set(registerCIndex,0x01);
            case 0xCBC2://SET0d
                this.set(registerDIndex,0x01);
            case 0xCBC3://SET0e
                this.set(registerEIndex,0x01);
            case 0xCBC4://SET0h
                this.set(registerHIndex,0x01);
            case 0xCBC5://SET0l
                this.set(registerLIndex,0x01);
            case 0xCBC6://SET0hl
                this.setHL(0x01);
            case 0xCBC7://SET0a
                this.set(registerAIndex,0x01);

            case 0xCBC8://SET1b
                this.set(registerBIndex,0x02);
            case 0xCBC9://SET1c
                this.set(registerCIndex,0x02);
            case 0xCBCA://SET1d
                this.set(registerDIndex,0x02);
            case 0xCBCB://SET1e
                this.set(registerEIndex,0x02);
            case 0xCBCC://SET1h
                this.set(registerHIndex,0x02);
            case 0xCBCD://SET1l
                this.set(registerLIndex,0x02);
            case 0xCBCE://SET1hl
                this.setHL(0x02);
            case 0xCBCF://SET1a
                this.set(registerAIndex,0x02);


            case 0xCBD0://SET2b
                this.set(registerBIndex,0x04);
            case 0xCBD1://SET2c
                this.set(registerCIndex,0x04);
            case 0xCBD2://SET2d
                this.set(registerDIndex,0x04);
            case 0xCBD3://SET2e
                this.set(registerEIndex,0x04);
            case 0xCBD4://SET2h
                this.set(registerHIndex,0x04);
            case 0xCBD5://SET2l
                this.set(registerLIndex,0x04);
            case 0xCBD6://SET2hl
                this.setHL(0x04);
            case 0xCBD7://SET2a
                this.set(registerAIndex,0x04);

            case 0xCBD8://SET3b
                this.set(registerBIndex,0x08);
            case 0xCBD9://SET3c
                this.set(registerCIndex,0x08);
            case 0xCBDA://SET3d
                this.set(registerDIndex,0x08);
            case 0xCBDB://SET3e
                this.set(registerEIndex,0x08);
            case 0xCBDC://SET3h
                this.set(registerHIndex,0x08);
            case 0xCBDD://SET2l
                this.set(registerLIndex,0x08);
            case 0xCBDE://SET3hl
                this.setHL(0x08);
            case 0xCBDF://SET3a
                this.set(registerAIndex,0x08);



            case 0xCBE0://SET4b
                this.set(registerBIndex,0x10);
            case 0xCBE1://SET4c
                this.set(registerCIndex,0x10);
            case 0xCBE2://SET4d
                this.set(registerDIndex,0x10);
            case 0xCBE3://SET4e
                this.set(registerEIndex,0x10);
            case 0xCBE4://SET4h
                this.set(registerHIndex,0x10);
            case 0xCBE5://SET4l
                this.set(registerLIndex,0x10);
            case 0xCBE6://SET4hl
                this.setHL(0x10);
            case 0xCBE7://SET4a
                this.set(registerAIndex,0x10);

            case 0xCBE8://SET5b
                this.set(registerBIndex,0x20);
            case 0xCBE9://SET5c
                this.set(registerCIndex,0x20);
            case 0xCBEA://SET5d
                this.set(registerDIndex,0x20);
            case 0xCBEB://SET5e
                this.set(registerEIndex,0x20);
            case 0xCBEC://SET5h
                this.set(registerHIndex,0x20);
            case 0xCBED://SET5l
                this.set(registerLIndex,0x20);
            case 0xCBEE://SET5hl
                this.setHL(0x20);
            case 0xCBEF://SET5a
                this.set(registerAIndex,0x20);


            case 0xCBF0://SET6b
                this.set(registerBIndex,0x40);
            case 0xCBF1://SET6c
                this.set(registerCIndex,0x40);
            case 0xCBF2://SET6d
                this.set(registerDIndex,0x40);
            case 0xCBF3://SET4e
                this.set(registerEIndex,0x40);
            case 0xCBF4://SET4h
                this.set(registerHIndex,0x40);
            case 0xCBF5://SET4l
                this.set(registerLIndex,0x40);
            case 0xCBF6://SET4hl
                this.setHL(0x40);
            case 0xCBF7://SET4a
                this.set(registerAIndex,0x40);

            case 0xCBF8://SET7b
                this.set(registerBIndex,0x80);
            case 0xCBF9://SET7c
                this.set(registerCIndex,0x80);
            case 0xCBFA://SET7d
                this.set(registerDIndex,0x80);
            case 0xCBFB://SET7e
                this.set(registerEIndex,0x80);
            case 0xCBFC://SET7h
                this.set(registerHIndex,0x80);
            case 0xCBFD://SET7l
                this.set(registerLIndex,0x80);
            case 0xCBFE://SET7hl
                this.setHL(0x80);
            case 0xCBFF://SET7a
                this.set(registerAIndex,0x80);

        }

    }

    private void setHL(int bit) {
        int hl = memoryManagementUnit.readWord((this.allMainRegisters[registerHIndex]<<8)+this.allMainRegisters[registerLIndex]);
        hl|=bit;
        memoryManagementUnit.writeWord((this.allMainRegisters[registerHIndex]<<8)+this.allMainRegisters[registerLIndex],hl);
        this.machineCycle = 4;
    }

    private void set(int registerIndex, int bit) {
        this.allMainRegisters[registerIndex] = this.allMainRegisters[registerIndex]|bit;        
        this.machineCycle=2;
    }

    private void resetBitHL(int mask) {
        int hl = memoryManagementUnit.readWord((this.allMainRegisters[registerHIndex]<<8)+this.allMainRegisters[registerLIndex]);
        hl&=mask;
        memoryManagementUnit.writeWord((this.allMainRegisters[registerHIndex]<<8)+this.allMainRegisters[registerLIndex],hl);
        this.machineCycle = 4;
    }

    private void setBitHL(int bit) {
        this.allMainRegisters[registerFIndex]&=0x1F;
        this.allMainRegisters[registerFIndex]|=20;
        this.allMainRegisters[registerFIndex] = (memoryManagementUnit.readWord(((this.allMainRegisters[registerHIndex]<<8)+this.allMainRegisters[registerLIndex]))&bit) !=0 ?0:0x80;
        this.machineCycle=3;
    }

    private void resetBit(int registerIndex, int bit) {
        this.allMainRegisters[registerIndex] =this.allMainRegisters[registerIndex] &bit;
        this.machineCycle = 2;

    }    
    private void setBit(int registerIndex, int bit) {
        this.allMainRegisters[registerFIndex]&=0x1F;
        this.allMainRegisters[registerFIndex]|=0x20;
        this.allMainRegisters[registerFIndex]=(this.allMainRegisters[registerIndex]&bit)!=0?0:0x80;
        this.machineCycle=2;

    }

    private void shiftRightCarryMSB(int registerIndex) {
        int registerValue = this.allMainRegisters[registerIndex];
        int firstBit = ((registerValue & 1) != 0) ? 0x10 : 0;
        registerValue = ((registerValue >> 1)) & 255;
        this.allMainRegisters[registerFIndex] = registerValue != 0 ? 0 : 0x80;
        this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerFIndex] & 0xEF) + firstBit;
        this.machineCycle = 2;

        this.allMainRegisters[registerIndex]=registerValue;
    }

    private void swap(int registerIndex) {
        int registerValue = this.allMainRegisters[registerIndex];
        registerValue = ((registerValue & 0xF) << 4) | ((registerValue & 0xF0) >> 4);
        this.allMainRegisters[registerFIndex] = registerValue != 0 ? 0 : 0x80;
        this.machineCycle = 1;

        this.allMainRegisters[registerIndex]= registerValue;
    }

    private void shiftRightCarry(int registerIndex) {
        int registerValue = this.allMainRegisters[registerIndex];
        int lastBit = registerValue & 0x80;
        int firstBit = ((registerValue & 1) != 0) ? 0x10 : 0;
        registerValue = ((registerValue >> 1) + lastBit) & 255;
        this.allMainRegisters[registerFIndex] = (registerValue != 0) ? 0 : 0x80;
        this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerFIndex] & 0xEF) + firstBit;
        this.machineCycle = 2;
        this.allMainRegisters[registerIndex]= registerValue;
    }

    private void shiftLeftCarry(int registerIndex) {
        int registerValue = this.allMainRegisters[registerIndex];
        int fifthBit = ((registerValue & 0x80) != 0) ? 0x10 : 0;
        registerValue = (registerValue << 1) & 255;
        this.allMainRegisters[registerFIndex] = (registerValue != 0) ? 0 : 0x80;
        this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerFIndex] & 0xEF) + fifthBit;
        this.machineCycle = 2;
        this.allMainRegisters[registerIndex]= registerValue;

    }

    private void rotateRight(int registerIndex) {
        int registerValue = this.allMainRegisters[registerIndex];
        int fifthBit = (this.allMainRegisters[registerFIndex] & 0x10) != 0 ? 0x80 : 0;
        int firstBit = ((registerValue & 1) != 0) ? 0x10 : 0;
        registerValue = (registerValue >> 1) + fifthBit;
        registerValue &= 255;
        this.allMainRegisters[registerFIndex] = (registerValue != 0) ? 0 : 0x80;
        this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerFIndex] & 0xEF) + firstBit;
        this.machineCycle = 2;
        this.allMainRegisters[registerIndex]= registerValue;


    }

    private void rotateLeft(int registerIndex) {
        int registerValue = this.allMainRegisters[registerIndex];
        int fifthBit = (this.allMainRegisters[registerFIndex] & 0x10) != 0 ? 1 : 0;
        int lastBit = (registerValue & 0x80) != 0 ? 0x10 : 0;
        registerValue = (registerB << 1) + fifthBit;
        registerValue &= 255;
        this.allMainRegisters[registerFIndex] = (registerValue != 0) ? 0 : 0x80;
        this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerFIndex] & 0xEF) + lastBit;
        this.machineCycle = 2;
        this.allMainRegisters[registerIndex]= registerValue;

    }

    private void rotateRightCarry(int registerIndex) {
        int registerValue = this.allMainRegisters[registerIndex];
        int lastBit = ((registerValue & 0x1) != 0) ? 0x80 : 0;
        int fifthBit = ((registerValue & 0x1) != 0) ? 0x10 : 0;
        registerValue = (registerValue >> 1) + lastBit;
        registerValue &= 255;
        this.allMainRegisters[registerFIndex] = (registerValue != 0) ? 0 : 0x80;
        this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerFIndex] & 0xEF) + fifthBit;
        this.machineCycle = 2;
        this.allMainRegisters[registerIndex]= registerValue;

    }

    private void rotateLeftCarry(int registerIndex) {
        int registerValue = this.allMainRegisters[registerIndex];
        int lastBit = ((registerValue & 0x80) != 0) ? 1 : 0;
        int fifthBit = ((registerValue & 0x80) != 0) ? 0x10 : 0;
        registerValue = (registerValue << 1) + lastBit;
        registerValue &= 255;
        this.allMainRegisters[registerFIndex] = (registerValue != 0) ? 0 : 0x80;
        this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerFIndex] & 0xEF) + fifthBit;
        this.machineCycle = 2;
        this.allMainRegisters[registerIndex]= registerValue;


    }


    private void notImplemented(int operationCode) {

    }

    private void addJump(int flagToCheck, int flagValue) {
        int value = memoryManagementUnit.readDoubleWord(this.programCounter);
        //is that necessary ???
        if (value > 127) {
            value = -((~value + 1) & 255);
        }
        this.programCounter++;
        this.machineCycle = 2;
        if ((this.allMainRegisters[registerFIndex] & flagToCheck) == flagValue) {
            this.programCounter += value;
            this.machineCycle++;
        }
    }

    private void jump(int flagToCheck, int flagValue) {
        this.machineCycle = 3;
        if ((this.allMainRegisters[registerFIndex] & flagToCheck) == flagValue) {
            this.programCounter = memoryManagementUnit.readDoubleWord(this.programCounter);
            this.machineCycle++;
        } else {
            this.programCounter += 2;
        }

    }

    private void returnTo(int flagToCheck, int valueExpected) {
        this.machineCycle = 1;
        if ((this.allMainRegisters[registerFIndex] & flagToCheck) == valueExpected) {
            this.programCounter = memoryManagementUnit.readDoubleWord(this.programCounter);
            this.stackPointer += 2;
            this.machineCycle += 2;

        }
    }

    private void restart(int jumpTo) {
        this.saveState();
        this.stackPointer = -2;
        this.memoryManagementUnit.writeDoubleWord(this.stackPointer, this.programCounter);
        this.programCounter = jumpTo;
        this.machineCycle = 3;

    }

    private void push(int registerIndex, int registerIndex2) {
        this.stackPointer--;
        this.memoryManagementUnit.writeWord(this.stackPointer, this.allMainRegisters[registerIndex]);
        this.stackPointer--;
        this.memoryManagementUnit.writeWord(this.stackPointer, this.allMainRegisters[registerIndex2]);
        this.machineCycle = 3;
    }

    private void call(int flagToCheck, int flagValue) {
        this.machineCycle = 3;
        if ((this.allMainRegisters[registerFIndex] & flagToCheck) == flagValue) {
            this.stackPointer -= 2;
            this.memoryManagementUnit.writeDoubleWord(this.stackPointer, this.programCounter + 2);
            this.programCounter = memoryManagementUnit.readDoubleWord(this.programCounter);
            this.machineCycle += 2;
        } else {
            this.programCounter += 2;
        }

    }

    private void pop(int registerIndex, int registerIndex2) {
        this.allMainRegisters[registerIndex2]= memoryManagementUnit.readWord(this.stackPointer) & 255;
        this.stackPointer++;
        this.allMainRegisters[registerIndex]=memoryManagementUnit.readWord(this.stackPointer) & 255;
        this.stackPointer++;
    }

    private void cp(int registerIndex) {
        int newValue = this.allMainRegisters[registerAIndex];
        int registerValue = this.allMainRegisters[registerIndex];
        newValue -= registerValue;
        this.allMainRegisters[registerFIndex] = (newValue < 0) ? 0x50 : 0x40;
        newValue &= 255;
        if (newValue == 0) {
            this.allMainRegisters[registerFIndex] |= 0x80;
        }
        if (((this.allMainRegisters[registerAIndex] ^ registerValue ^ newValue) & 0x10) != 0) {
            this.allMainRegisters[registerFIndex] |= 0x20;
        }
        this.machineCycle = 1;
    }

    private void xor(int registerIndex) {
        this.allMainRegisters[registerAIndex] ^= this.allMainRegisters[registerIndex];
        this.allMainRegisters[registerAIndex] &= 255;
        this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerAIndex] != 0) ? 0 : 0x80;
        this.machineCycle = 1;
    }

    private void or(int registerIndex) {
        this.allMainRegisters[registerAIndex] |= this.allMainRegisters[registerIndex];
        this.allMainRegisters[registerAIndex] &= 255;
        this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerAIndex] != 0) ? 0 : 0x80;
        this.machineCycle = 1;
    }

    private void and(int registerIndex) {
        this.allMainRegisters[registerAIndex] &= this.allMainRegisters[registerIndex];
        this.allMainRegisters[registerAIndex] &= 255;
        this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerAIndex] != 0) ? 0 : 0x80;
        this.machineCycle = 1;
    }

    private void subc(int registerIndex) {
        int registerAValue = this.allMainRegisters[registerAIndex];
        int registerValueToSub = this.allMainRegisters[registerIndex];
        this.allMainRegisters[registerAIndex] -= registerValueToSub;
        this.allMainRegisters[registerAIndex] -= ((this.allMainRegisters[registerFIndex] & 0x10) != 0) ? 1 : 0;
        this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerAIndex] < 0) ? 0x50 : 0x40;
        this.allMainRegisters[registerAIndex] &= 255;
        if (this.allMainRegisters[registerAIndex] == 0) {
            this.allMainRegisters[registerFIndex] |= 0x80;
        }
        if (((this.allMainRegisters[registerAIndex] ^ registerValueToSub ^ registerAValue) & 0x10) != 0) {
            this.allMainRegisters[registerFIndex] |= 0x20;
        }
        this.machineCycle = 1;

    }

    private void sub(int registerIndex) {
        int registerAValue = this.allMainRegisters[registerAIndex];
        int registerValueToSub = this.allMainRegisters[registerIndex];
        this.allMainRegisters[registerAIndex] -= registerValueToSub;
        this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerAIndex] < 0) ? 0x50 : 0x40;
        this.allMainRegisters[registerAIndex] &= 255;
        if (this.allMainRegisters[registerAIndex] == 0) {
            this.allMainRegisters[registerFIndex] |= 0x80;
        }
        if (((this.allMainRegisters[registerAIndex] ^ registerValueToSub ^ registerAValue) & 0x10) != 0) {
            this.allMainRegisters[registerFIndex] |= 0x20;
        }
        this.machineCycle = 1;
    }

    private void addc(int registerIndex) {
        int registerAValue = this.allMainRegisters[registerAIndex];
        int registerToAdd = this.allMainRegisters[registerIndex];
        this.allMainRegisters[registerAIndex] += registerToAdd;
        this.allMainRegisters[registerAIndex] += ((this.allMainRegisters[registerFIndex] & 0x10) != 0) ? 1 : 0;
        this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerAIndex] > 255) ? 0x10 : 0;
        this.allMainRegisters[registerAIndex] &= 255;
        if (this.allMainRegisters[registerAIndex] == 0) {
            this.allMainRegisters[registerFIndex] |= 0x80;
        }
        if (((this.allMainRegisters[registerAIndex] ^ registerToAdd ^ registerAValue) & 0x10) != 0) {
            this.allMainRegisters[registerFIndex] |= 0x20;
        }
        this.machineCycle = 1;
    }

    private void updateFlagRegisterIncDec(int register) {
        this.allMainRegisters[registerFIndex] = register != 0 ? 0x00 : 0x80;
    }

    private void inc(int registerIndex) {
        int registerValue = this.allMainRegisters[registerIndex];
        registerValue = (registerValue + 1) & 255;
        this.allMainRegisters[registerIndex]= registerValue;
        this.updateFlagRegisterIncDec(registerValue);
        this.machineCycle = 1;
    }

    private void dec(int registerIndex) {
        int registerValue = this.allMainRegisters[registerIndex];
        registerValue = (registerValue - 1) & 255;
        this.allMainRegisters[registerIndex]= registerValue;

        this.updateFlagRegisterIncDec(registerValue);
        this.machineCycle = 1;
    }

    private void dec2(int registerIndex, int registerIndex2) {
        int register1 = this.allMainRegisters[registerIndex];
        int register2 = this.allMainRegisters[registerIndex2];
        register2 = (register2 - 1) & 255;
        if (register2 == 255) {
            register1 = (register1 - 1) & 255;
        }
        this.allMainRegisters[registerIndex]= register1;
        this.allMainRegisters[registerIndex2]= register2;
        this.machineCycle = 1;

    }

    private void inc2(int registerIndex, int registerIndex2) {
        int register1 = this.allMainRegisters[registerIndex];
        int register2 =this.allMainRegisters[registerIndex2];
        register2 = (register2 + 1) & 255;
        if (register2 == 0) {
            register1 = (register1 + 1);
            this.allMainRegisters[registerIndex]= register1;
        }
        this.allMainRegisters[registerIndex2]= register2;
        this.machineCycle = 1;
    }

    private void add2(int highByteRegister, int lowByteRegister, int highByteRegister2, int lowBytesRegister2) {

        int registerH1 = this.allMainRegisters[highByteRegister];
        int registerL1 = this.allMainRegisters[lowByteRegister];

        int registerH2 = this.allMainRegisters[highByteRegister2];
        int registerL2 = this.allMainRegisters[lowBytesRegister2];

        int hl = (registerH1 << 8) + registerL1;
        hl += (registerH2 << 8) + registerL2;
        if (hl > 65535) {
            this.allMainRegisters[registerFIndex] |= 0x10;
        } else {
            this.allMainRegisters[registerFIndex] &= 0xEF;
        }
        registerH1 = (hl >> 8) & 255;
        registerL1 = hl & 255;

        this.allMainRegisters[highByteRegister2]= registerH2;
        this.allMainRegisters[lowBytesRegister2]= registerL2;
        this.allMainRegisters[highByteRegister] = registerH1;
        this.allMainRegisters[lowByteRegister]  = registerL1;
        this.machineCycle = 3;

    }

    private void load2(int registerIndex1, int registerIndex2) {
        this.allMainRegisters[registerIndex2] =  memoryManagementUnit.readWord(this.programCounter) & 255;
        this.allMainRegisters[registerIndex1] =  memoryManagementUnit.readWord(this.programCounter + 1) & 255;
        this.programCounter += 2;
        this.machineCycle = 3;
    }

    private void load(int registerIndex) {
        this.allMainRegisters[registerIndex]= memoryManagementUnit.readWord(this.programCounter) & 255;
        this.programCounter++;
        this.machineCycle = 2;

    }

    private void loadTo(int destinationRegister, int fromRegister) {
        this.allMainRegisters[destinationRegister]= this.allMainRegisters[fromRegister];
    }


    private void loadHL(int registerIndex) {
        this.allMainRegisters[registerIndex]=memoryManagementUnit.readWord((this.allMainRegisters[registerHIndex] << 8) + this.allMainRegisters[registerLIndex]) & 255;
        this.machineCycle = 2;
    }

    private void add(int registerIndex) {

        int registerAValue = this.allMainRegisters[registerAIndex];
        int registerValueToAdd = this.allMainRegisters[registerIndex];
        this.allMainRegisters[registerAIndex] += registerValueToAdd;
        this.allMainRegisters[registerFIndex] = (this.allMainRegisters[registerAIndex] > 255) ? 0x10 : 0;
        this.allMainRegisters[registerAIndex] &= 255;
        if (this.allMainRegisters[registerAIndex] == 0) {
            this.allMainRegisters[registerFIndex] |= 0x80;
        }
        if (((this.allMainRegisters[registerAIndex] ^ registerValueToAdd ^ registerAValue) & 0x10) != 0) {
            this.allMainRegisters[registerFIndex] |= 0x20;
        }
        this.machineCycle = 1;

    }


    @Override
    public void setMemoryManagementUnit(MemoryManagementUnit memoryManagementUnit) {
        this.memoryManagementUnit = memoryManagementUnit;
    }

    public int getRegisterA() {
        return allMainRegisters[0];
    }

    public int getRegisterB() {
        return  allMainRegisters[1];
    }

    public int getRegisterC() {
        return  allMainRegisters[2];
    }

    public int getRegisterD() {
        return  allMainRegisters[3];
    }

    public int getRegisterE() {
        return  allMainRegisters[4];
    }

    public int getRegisterF() {
        return  allMainRegisters[5];
    }

    public int getRegisterH() {
        return  allMainRegisters[6];
    }

    public int getRegisterL() {
        return  allMainRegisters[7];
    }

    public int getStackPointer() {
        return stackPointer;
    }

    public int getProgramCounter() {
        return programCounter;
    }

    public int getMachineCycle() {
        return machineCycle;
    }

    public int getClockMachineCycle() {
        return clockMachineCycle;
    }

    public int getInterruptMode() {
        return interruptMode;
    }

    public int getHalt() {
        return halt;
    }
}
