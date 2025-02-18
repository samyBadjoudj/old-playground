package com.samy.gameboy.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

/**
 * Created by samy badjoudj
 */
public class RomReaderImpl implements RomReader {

    @Override
    public InputStream getRom(String fileName) throws IOException {

        final FileInputStream romFile = new FileInputStream(new File(fileName));
        RomFileTypeEnum typeEnum = getRomFileTypeEnum(fileName);

        switch (typeEnum){

            case RAW:
                return romFile;
            case ZIP:
                return new ZipInputStream(romFile);
            case GZ:
                return new GZIPInputStream(romFile);
            default:
                return romFile;

        }

    }

    private RomFileTypeEnum getRomFileTypeEnum(String fileName) {

       final String fileNameUpperCase = fileName.toUpperCase();

        if(fileNameUpperCase.contains(RomFileTypeEnum.ZIP.name())
           || fileNameUpperCase.contains(RomFileTypeEnum.JAR.name())){
            return  RomFileTypeEnum.ZIP;
        }else if (fileNameUpperCase.contains(RomFileTypeEnum.GZ.name())){
            return  RomFileTypeEnum.GZ;
        }else{
            return  RomFileTypeEnum.RAW;
        }

    }


}
