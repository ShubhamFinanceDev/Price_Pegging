package com.price.pegging.Model;

                                                //     NOTE.......
import lombok.Data;                        //This MODEL class is made by shagun for
                                            //    getDataForMap controller....
import java.util.List;


@Data
public class CommonDsaExportData
{


    private String msg;

    private String code;

    private List<DsaExportData> dsaExportData;
}
