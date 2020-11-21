<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="automoviles">
    <form:form modelAttribute="automovil" class="form-horizontal" id="add-auto-form" action="automoviles/edit/${automovil.id}">
        
            <petclinic:inputField label="Marca" name="marca"/>
            <petclinic:inputField label="Modelo" name="modelo"/>
            <petclinic:inputField label="Número de plazas" name="numPlazas"/>
            <petclinic:inputField label="Kilómetros recorridos" name="kmRecorridos"/>   
             <p> (Falta meter al trabajador aquí) </p>
             <input type="hidden" name="autoId" id="autoId" value="${automovil.id}"/>
                    
             <button class="btn btn-default" type="submit">Actualizar automóvil</button>
    </form:form>
</petclinic:layout>