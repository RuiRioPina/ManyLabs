package app.domain.store;

import app.domain.model.DateInterval;
import app.domain.model.Test;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class LabCoordinatorStore {

    List<Test> tests;

    public DateInterval setInterval(String sDate, String eDate){
        return new DateInterval(sDate,eDate);
    }

    public int getNumberOfTR(List<Test> tests){
        int count=0;
        for (Test t : tests) {
            if (t.getValidationDate()!=null ){
                count++;
            }
        }
        return count;
    }



    public List<Test> getTestsWithoutResults() {
        List<Test> complete = new ArrayList<>();
        for (Test t : this.tests) {
            if (t.getSamplesCollectionDate() != null && t.getChemicalAnalysisDate() != null && t.getDiagnosisDate() != null && t.getValidationDate() == null) {
                complete.add(t);
            }
        }
        return complete;
    }

    public int getNumberOfTNR(List<Test> tests){
        int count=0;
        for (Test t : tests) {
            if (t.getSamplesCollectionDate()!=null){
                count++;
            }
        }
        return count;
    }

    public int[] maxSubArraySum(int[] list) {
        int size=list.length;
        int max_so_far = Integer.MIN_VALUE,
                max_ending_here = 0,start = 0,
                end = 0, s = 0;

        for (int i = 0; i < size; i++)
        {
            max_ending_here += list[i];

            if (max_so_far < max_ending_here)
            {
                max_so_far = max_ending_here;
                start = s;
                end = i;
            }

            if (max_ending_here < 0)
            {
                max_ending_here = 0;
                s = i + 1;
            }
        }

        int[] places=new int[end-start+1];
        for (int i = 0; i < places.length; i++) {
            places[i]=start;
            start++;
        }

        return places;
    }

    public int[] listMax(Calendar s,Calendar e){
        int bHour=8;
        int eHour=20;
        int countTests=0;
        int countResults=0;
        int count=0;
        int[]dif=new int[144];
        Calendar j = new GregorianCalendar(s.get(Calendar.YEAR),s.get(Calendar.MONTH),s.get(Calendar.DAY_OF_MONTH),bHour,30,0);
        List<Test>copy=tests;
        for (; s.before(e); s.add(Calendar.MINUTE, +30)) {
            countTests=0;
            countResults=0;
            j.add(Calendar.MINUTE,30);
            if ((s.get(Calendar.HOUR_OF_DAY)==eHour && (s.get(Calendar.MINUTE)==0)) || s.get(Calendar.DAY_OF_WEEK)==0){
                s.add(Calendar.DAY_OF_MONTH,+1);
                s.set(Calendar.HOUR_OF_DAY,bHour);
                j.add(Calendar.DAY_OF_MONTH,+1);
                j.set(Calendar.HOUR_OF_DAY,bHour);
            }else{
                for (Test t : copy){
                    if(t.getRegistrationDate().after(s) && t.getRegistrationDate().after(j)){
                        countTests++;
                        copy.remove(t);
                    }
                    if (t.getValidationDate().after(s) && t.getValidationDate().after(j)) {
                        countResults++;
                        copy.remove(t);

                    }
                }
                dif[count]=countTests-countResults;
                count++;
            }

        }
        return dif;
    }


    public boolean isTestInInterval (Test t, Calendar str, Calendar end) {
        Calendar test = t.getRegistrationDate();
        return test.after(str) && test.before(end);
    }

    public Calendar tStringToCalendar (String txt) {
        String message="Wrong date";
        try {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date dt = df.parse(txt);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dt);
            return calendar;
        } catch (Exception ex) {
            System.out.println(message);
        }
        return null;
    }

    public Calendar newCategory(String Date) {
        return tStringToCalendar(Date);
    }
}