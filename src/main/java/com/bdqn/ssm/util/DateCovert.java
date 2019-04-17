package com.bdqn.ssm.util;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
* 日期转换器
*S:source 要转换的源类型
* */
public class DateCovert implements Converter<String, Date> {


    public Date convert(String source) {
        Date date=null;
        try {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date=sdf.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }
}
