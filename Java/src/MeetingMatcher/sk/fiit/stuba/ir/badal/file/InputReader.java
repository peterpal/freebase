package sk.fiit.stuba.ir.badal.file;

import sk.fiit.stuba.ir.badal.exception.InputFileNotSetException;

import java.io.*;

/**
 * Created by delve on 11/10/14.
 */
public class InputReader {

    private static String DATA_PATH = "./data";

    private BufferedReader reader     = null;
    private File           lastFile   = null;
    private String         lastOutput = null;

    public InputReader(String fileName) throws IOException {
        this.resetReader(fileName);
    }

    public void resetReader(String fileName) throws IOException {
        if (this.reader != null) {
            this.reader.close();
        }

        this.lastFile = new File(InputReader.DATA_PATH + File.separator + fileName);
        this.reader = new BufferedReader(new FileReader(this.lastFile));
    }

    public String readLine() throws InputFileNotSetException, IOException {
        if (this.lastFile == null) {
            throw new InputFileNotSetException();
        }

        return this.reader.readLine();
    }

    public String getOutput() {
        return this.lastOutput;
    }

    public void closeReader() throws IOException {
        this.reader.close();
    }
}
