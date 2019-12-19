package com.jvs.gd.baseus;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Getter;
import lombok.Setter;


/** Essa classe fara a auditoria das classes que herdarem ela, registrando sua data de criação e 
  * alteração */
public abstract class BaseUsAbstractModel {

    //@Id @Getter @Setter
    //private String id;

    @CreatedDate
    private Date createdDate;

    @LastModifiedDate
    private Date lastModifiedDate;
    
    // TODO - IMPLEMENTAR USUARIO LOGADO

}