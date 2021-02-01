<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="reservas">
    <form:form modelAttribute="reserva" class="form-horizontal" id="add-reserva-form" action="/reservas/redirigirEditRutaForm">
        
       <jsp:include page="/WEB-INF/jsp/reservas/rutaForm.jsp"/>
            
		<br><br>
        <label for="fechaSalida">Fecha de salida</label>
         <input type="date" name="fechaSalida"  id="fechaSalida" value="<fmt:formatDate pattern = "yyyy-MM-dd" value = "${reserva.fechaSalida}" />"/>     
      <input type="hidden" name="id" id="id" value="${reserva.id}"/>
       
		<label for="horaSalida">Hora de salida</label> 
      
        <input type="time"  name="horaSalida" id="horaSalida" value="<fmt:formatDate type = "time" pattern="HH:mm" value = "${reserva.horaSalida}" />"/>
       	<input type="hidden" name="horasEspera" id="horasEspera" value="${reserva.horasEspera}"/>
       	<input type="hidden" name="plazas_Ocupadas" id="plazas_Ocupadas" value="${reserva.plazas_Ocupadas}"/>
         <input type="hidden" name="descripcionEquipaje" id="descripcionEquipaje" value="${reserva.descripcionEquipaje}"/>
         <input type="hidden" name="estadoReserva" id="estadoReserva" value="${reserva.estadoReserva.id}"/>
       
		<br><br>
          
		<button class="btn btn-default" type="submit" name="action" value="recalcularReserva">Recalcular Reserva</button> 
		<br><br>
		*se recalculará el nuevo precio teniendo en cuenta la tarifa que se encuentre activa en este momento		 
    </form:form>    
    
</petclinic:layout>