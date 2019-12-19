package com.jvs.gd.models;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonFormat;



public abstract class BaseUsAbstractModel {


    @CreatedDate
    private Date createdDate;


    @LastModifiedDate
    private Date lastModifiedDate;
    
    // TODO - IMPLEMENTAR USUARIO LOGADO

}