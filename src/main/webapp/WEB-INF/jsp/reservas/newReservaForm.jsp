<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="reservas">
    <form:form modelAttribute="reserva" class="form-horizontal" id="add-reserva-form" action="/reservas/redirigirNewReservaForm">
        
         <input type="hidden" name="numCiudadesIntermedias" value="${numCiudadesIntermedias}"/>
        <p> ${usuario}</p>
       
      <jsp:include page="/WEB-INF/jsp/reservas/rutaForm.jsp"/>

		<br><br>
           
        <!--  Meter dentro del binding la fecha y hora de salida aquí porque en el tag hay conflictos -->
            <label for="fechaSalida">Fecha de salida</label>
            <input type="date" name="fechaSalida"  id="fechaSalida" value="<fmt:formatDate pattern = "yyyy-MM-dd" value = "${reserva.fechaSalida}" />"/>     
           
           
           <label for="horaSalida">Hora de salida</label> 
           <input type="time"  name="horaSalida" id="horaSalida" value="<fmt:formatDate type = "time" pattern="HH:mm" value = "${reserva.horaSalida}" />"/>
           
           
           
            <petclinic:inputField label="Plazas ocupadas" name="plazas_Ocupadas"/>
            <petclinic:inputField label="Descripción del equipaje" name="descripcionEquipaje"/>
     		
    	    <!-- Vamos a tener 2 botones submit  en el mismo formulario  -->
    	       
		<button class="btn btn-default" type="submit" name="action" value="continuar"> Calcular Precio <span class="glyphicon glyphicon-menu-right" aria-hidden="false"></span></button> 		 
    </form:form>    
    
</petclinic:layout>