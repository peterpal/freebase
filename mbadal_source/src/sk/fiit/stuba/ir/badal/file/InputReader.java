package sk.fiit.stuba.ir.badal.file;

import sk.fiit.stuba.ir.badal.exception.InputFileNotSetException;

import java.io.*;

/**
 * Created by delve on 11/10/14.
 */
public class InputReader {

    private static String DATA_PATH = "./data/";

    private BufferedReader reader     = null;
    private File           lastFile   = null;
    private String         lastOutput = null;

    public InputReader(String fileName) {
        this.lastFile = new File(InputReader.DATA_PATH + fileName);
        try {
            this.reader = new BufferedReader(new FileReader(this.lastFile));
        } catch (FileNotFoundException e) {
            //log
            System.out.println("Error while reading file");
            System.exit(1);
        }
    }

    public void read() throws InputFileNotSetException, IOException {
        if (this.lastFile == null) {
            throw new InputFileNotSetException();
        }

        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = this.reader.readLine()) != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
        }
        this.lastOutput = sb.toString();

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
}
