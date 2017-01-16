package necer.edit;

import java.io.PrintWriter;

public class Utils {

    public static void runCmd(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            PrintWriter pw = new PrintWriter(process.getOutputStream());
            pw.println(cmd);
            pw.flush();
            pw.close();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
