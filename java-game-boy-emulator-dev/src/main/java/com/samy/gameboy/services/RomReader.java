package com.samy.gameboy.services;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by samy badjoudj.
 */
public interface RomReader {

   public   InputStream getRom(String fileName) throws IOException;
}
