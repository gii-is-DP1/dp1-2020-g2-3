<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="trabajadores">
    <form:form modelAttribute="trabajador" class="form-horizontal" id="add-auto-form">
        	<h2> Datos trabajador </h2>
            <petclinic:inputField label="DNI" name="dni"/>
            <petclinic:inputField label="Nombre" name="nombre"/>
            <petclinic:inputField label="Apellidos" name="apellidos"/>
            <petclinic:inputField label="Correo electrónico" name="email"/>
            <petclinic:inputField label="Teléfono" name="telefono"/>
            <petclinic:inputField label="Nombre de usuario" name="user.username"/>
            <petclinic:inputField label="Contraseña" name="user.password"/>
            
             <label for="user.enabled">Activo:</label>
		<select name="user.enabled" id="user.enabled">
			
            <option value="true"  >Activo</option>
				
                
            <option value="false" selected >No Activo</option>
		                      
		</select>
		
            
       
        <label for="tipoTrabajador">Tipo trabajador:</label>
		<select name="tipoTrabajador" id="tipoTrabajador">
				 <c:forEach items="${tipostrabajador}" var="tipoTrabajador">
				
                    <option value="${tipoTrabajador.id}"  >
				 ${tipoTrabajador.name}</option>
             
				 
    	        </c:forEach>
		</select>
           
          
           	
           	<h2> Datos contrato </h2>
           
          	<petclinic:inputField label="Salario Mensual" name="contrato.salarioMensual"/>
           
           	<label for="fechaInicio">Fecha Inicio</label>
            <input type="date" name="contrato.fechaInicio"  id="fechaInicio" value="<fmt:formatDate pattern = "yyyy-MM-dd" value = "${contrato.fechaInicio}" />"/>
        
        	<label for="fechaFin">Fecha Fin</label>
        	<input type="date" name="contrato.fechaFin"  id="fechaFin" value="<fmt:formatDate pattern = "yyyy-MM-dd" value = "${contrato.fechaFin}" />"/>
       
          	
  			
			     
	 <!--  El trabajador correspondiente aparecerá seleccionado por defecto-->
	 

             <input type="hidden" name="id" id="id" value="${trabajador.id}"/>
             <c:choose>
                    <c:when test="${trabajador['new']}">
                        <button class="btn btn-default" type="submit">Añadir Trabajador</button>
                    </c:when>
                    
                </c:choose>
    </form:form>
</petclinic:layout>
