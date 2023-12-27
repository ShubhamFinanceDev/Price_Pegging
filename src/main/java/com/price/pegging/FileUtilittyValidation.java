package com.price.pegging;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import static org.apache.poi.ss.util.SheetUtil.getCell;

@Service
public class FileUtilittyValidation {
    public boolean dsaFileFormat(Row headerRow) {

        Boolean matched = true;
        String errorMsg="";


        for (int i = 0; i < 13; i++) {
            Cell cell= headerRow.getCell(i);
            errorMsg = (cell == null || cell.getCellType() == CellType.BLANK) ? "file upload error due to row no " + i + 1 + " is empty" : "";

           if(errorMsg.isEmpty() && matched==true ) {

       //        System.out.println("Header value" + cell.toString() + "\n");
               String cellName = cell.toString();
               switch (cellName) {
                   case "S.No":
                       matched = true;
                       break;
                   case "DASpplicDAStion_Number":
                       matched = true;
                       break;
                   case "Product":
                       matched = true;
                       break;
                   case "First Disbursal Date":
                       matched = true;
                       break;
                   case "Property Address":
                       matched = true;
                       break;
                   case "Property Pincode":
                       matched = true;
                       break;
                   case "Region":
                       matched = true;
                       break;
                   case "Zone/Dist":
                       matched = true;
                       break;
                   case "Locations":
                       matched = true;
                       break;

                   case "Rate Per sqft":
                       matched = true;
                       break;
                   case "Property Type":
                       matched = true;
                       break;
                   case "Lattitude":
                       matched = true;
                       break;
                   case "Longitude":
                       matched = true;
                       break;
                   default:
                       matched=false;
                       break;

               }
           }
           else
           {
               matched=false;
           }
        }

            return matched;


        }


    public boolean pricePeggingFileFormat(Row headerRow) {

        Boolean matched = true;
        String errorMsg="";

        for (int i = 0; i < 6; i++) {
            Cell cell= headerRow.getCell(i);
            errorMsg = (cell == null || cell.getCellType() == CellType.BLANK) ? "file upload error due to row no " + i + 1 + " is empty" : "";

            if(errorMsg.isEmpty() && matched==true) {

                System.out.println("Header value" + cell.toString() + "\n");
                String cellName = cell.toString();
                switch (cellName) {

                    case "Zone/Dist":
                        matched = true;
                        break;
                    case "Locations":
                        matched = true;
                        break;

                    case "Minimum Rate":
                        matched = true;
                        break;
                    case "Maximum Rate":
                        matched = true;
                        break;
                    case "Average Rate":
                        matched = true;
                        break;
                    case "Pincode":
                        matched = true;
                        break;
                    default:
                        matched=false;
                        break;
                }
            }
            else
            {
                matched=false;
            }
        }

        return matched;


    }
}
