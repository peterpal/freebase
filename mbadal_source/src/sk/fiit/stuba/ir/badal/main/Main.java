package sk.fiit.stuba.ir.badal.main;

/**
 * Created by delve on 11/10/14.
 */
public class Main {
    public static void main(String[] args) {
        /*InputReader reader = new InputReader("sample_input_freebase_date-of-births.txt");
        String mSatanId = "04nfrq";
        String mSatanDate = null;
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                if (!Parser.containsId(line, mSatanId)) {
                    continue;
                }
                mSatanDate = Parser.extractDate(line);
            }
        } catch (InputFileNotSetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(mSatanDate);*/
        /*Person p = new Person("Test", "1989-12-05", null);
        Person p2 = new Person("test 2", "2014-10-12", null);
        PersonCollection col = new PersonCollection(p, p2);
        MeetingMatcher matcher = new MeetingMatcher(col);
        System.out.println(matcher.couldHaveMet());*/
        String miroslavSatanId = "04nfrq";
        String johnClayton  = "0zn10r8";
        Controller cont = new Controller(miroslavSatanId, johnClayton);
        cont.setUpModels();
    }
}
