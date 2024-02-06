<<<<<<< HEAD

=======
package com.price.pegging.Utilitty;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DateFormatUtility {
    public String changeDateFormate(String uploadDate) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String outputDateString="";
        try {
            Date inputDate = originalFormat.parse(uploadDate);
            SimpleDateFormat desiredFormat = new SimpleDateFormat("yyyy-MM-dd");

             outputDateString = desiredFormat.format(inputDate);

            System.out.println("Original Date: " + uploadDate);
            System.out.println("Converted Date: " + outputDateString);

        } catch (Exception e) {
            System.out.println(e);
        }
        return outputDateString;
    }
}
>>>>>>> development
