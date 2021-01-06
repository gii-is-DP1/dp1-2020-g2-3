<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="trabajadores">
    <form:form modelAttribute="trabajador" class="form-horizontal" id="add-auto-form">
        
            <petclinic:inputField label="DNI" name="dni"/>
            <petclinic:inputField label="Nombre" name="nombre"/>
            <petclinic:inputField label="Apellidos" name="apellidos"/>
            <petclinic:inputField label="Correo electrónico" name="email"/>
            <petclinic:inputField label="Teléfono" name="telefono"/>
          
         
          
			     
	 <!--  El trabajador correspondiente aparecerá seleccionado por defecto-->
	 

             <input type="hidden" name="id" id="id" value="${trabajador.id}"/>
             <c:choose>
                    <c:when test="${trabajador['new']}">
                        <button class="btn btn-default" type="submit">Añadir Trabajador</button>
                    </c:when>
                    
                </c:choose>
    </form:form>
</petclinic:layout>