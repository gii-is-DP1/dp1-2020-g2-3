<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="contratos">
    <form:form modelAttribute="contrato" class="form-horizontal" id="add-auto-form">
        
            <petclinic:inputField label="Salario Mensual" name="salarioMensual"/>
            <petclinic:inputField label="Fecha Inicio" name="fechaInicio"/>
            <petclinic:inputField label="Fecha Fin" name="fechaFin"/>
            <petclinic:inputField label="Trabajador" name="trabajador_id"/>
            
			     
	 <!--  El trabajador correspondiente aparecerá seleccionado por defecto-->
	 

             <input type="hidden" name="id" id="id" value="${contrato.id}"/>
             <c:choose>
                    <c:when test="${contrato['new']}">
                        <button class="btn btn-default" type="submit">Añadir Contrato</button>
                    </c:when>
                    
                </c:choose>
    </form:form>
</petclinic:layout>
