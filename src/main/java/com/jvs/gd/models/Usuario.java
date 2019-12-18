package com.jvs.gd.models;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

import com.jvs.gd.baseus.BaseUsAbstractModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false) 
@Document
public class Usuario extends BaseUsAbstractModel implements Serializable  {

	private static final long serialVersionUID = 1L;
	
/** @Id - id da classe implementado pela superclass de auditoria AbstractModel */
	private String nome;
	private String senha;
	private String email;
}
