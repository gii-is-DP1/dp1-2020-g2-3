<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="automoviles">
    <form:form modelAttribute="automovil" class="form-horizontal" id="add-auto-form">
        
            <petclinic:inputField label="Marca" name="marca"/>
            <petclinic:inputField label="Modelo" name="modelo"/>
            <petclinic:inputField label="Número de plazas" name="numPlazas"/>
            <petclinic:inputField label="Kilómetros recorridos" name="kmRecorridos"/>
          
			     
	 <!--  El trabajador correspondiente aparecerá seleccionado por defecto-->
	 

             <input type="hidden" name="id" id="id" value="${automovil.id}"/>
             <c:choose>
                    <c:when test="${automovil['new']}">
                        <button class="btn btn-default" type="submit">Añadir automóvil</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Actualizar automóvil</button>
                    </c:otherwise>
                </c:choose>
    </form:form>
</petclinic:layout>