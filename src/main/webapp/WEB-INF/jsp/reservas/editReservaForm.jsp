<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="reservas">
    <form:form modelAttribute="reserva" class="form-horizontal" id="add-reserva-form" action="/reservas/redirigirEditReservaForm">
        
       
        
       <h2>Ruta establecida</h2>
               <input type="hidden" name="id" id="id" value="${reserva.id}"/>
       
        <input type="hidden" name="numCiudadesIntermedias" id="numCiudadesIntermedias" value="${numCiudadesIntermedias}"/>
      <jsp:include page="/WEB-INF/jsp/reservas/mostrarRuta.jsp"/>

      <br>
      
     <petclinic:inputField label="Kilómetros totales" name="numKmTotales"/>
             <p>  Duración del viaje:  ${horasRutaCliente} horas y ${minutosRutaCliente} minutos </p> <br>
             
            <input type="hidden" name="horasRutaCliente" id="horasRutaCliente" value="${horasRutaCliente}"/>
              <input type="hidden" name="minutosRutaCliente" id="minutosRutaCliente" value="${minutosRutaCliente}"/>
      
<button class="btn btn-info" type="submit" name="action" value="editarRuta"><span class="glyphicon glyphicon-edit" aria-hidden="false"></span> Editar Ruta  </button> 
<br><br>
       <p style="border:3px; border-style:solid; border-color:#000000; background-color:#FFF699">
       *Para el cálculo de kilómetros totales se tiene en cuenta el <span style="color:green">  trayecto que realiza el taxista para llegar al origen </span>,
       así como el de <span style="color:green"> vuelta hasta su localidad (Zahínos) </span> <br>
       **Los trayectos en verde no le corresponden al cliente <br>
      ***La duración del viaje, así como la fecha estimada de llegada, corresponde a la ruta realizada por el cliente (Trayectos en negro)</p>
       		
      			
      <br>
       <h2>Fecha de Salida</h2>
       
        <label for="fechaSalida">Fecha de salida</label>
         <input type="date" name="fechaSalida"  id="fechaSalida" value="<fmt:formatDate pattern = "yyyy-MM-dd" value = "${reserva.fechaSalida}" />"/>     
      
       
		<label for="horaSalida">Hora de salida</label> 
           <input type="time"  name="horaSalida" id="horaSalida" value="<fmt:formatDate type = "time" pattern="HH:mm" value = "${reserva.horaSalida}" />"/>
           
		<h2>Fecha Estimada de Llegada</h2>

		
		 <label for="fechaLlegada">Fecha de llegada</label>
            <input type="date" name="fechaLlegada"  id="fechaLlegada" value="<fmt:formatDate pattern = "yyyy-MM-dd" value = "${reserva.fechaLlegada}" />"/>  
		
		<label for="horaLlegada">Hora de llegada</label> 
           <input type="time"  name="horaLlegada" id="horaLlegada" value="<fmt:formatDate type = "time" pattern="HH:mm" value = "${reserva.horaLlegada}" />"/>

        <h2> Detalles de la reserva </h2>
        
      
        
        
         	<petclinic:inputField label="Horas de espera" name="horasEspera"/>
            <petclinic:inputField label="Plazas ocupadas" name="plazas_Ocupadas"/>
            <petclinic:inputField label="Descripción del equipaje" name="descripcionEquipaje"/>
			
			
 		<petclinic:inputField label="Precio Total" name="precioTotal"/>
 		<br><br>
		 <h2>Tarifa utilizada (Inmutable):</h2>
     	  <p> Precio por Kilómetro: <c:out value="${reserva.tarifa.precioPorKm} euros"></c:out> </p>
     	 <p> Precio por horas de espera: <c:out value="${reserva.tarifa.precioEsperaPorHora} euros"></c:out></p>
     	 <p> Precio por horas de espera: <c:out value="${reserva.tarifa.precioEsperaPorHora} euros"></c:out></p>
     	 <p> Porcentaje IVA Repercutido: <c:out value="${reserva.tarifa.porcentajeIvaRepercutido} %"></c:out></p>
     	 <br> <br>
     	 
 		  <label for="estadoReserva">Estado de la reserva:</label>
		<select required="true" name="estadoReserva" id="estadoReserva">
				 <c:forEach items="${estadosReserva}" var="estado">
				 <c:choose>
                    <c:when test="${estado.id == reserva.estadoReserva.id}">
                        <option value="${estado.id}" selected > <c:out value="${estado.name}"></c:out></option>
                    </c:when>
                    <c:otherwise>
                        <option value="${estado.id}"> ${estado.name}</option>
                    </c:otherwise>
               	 </c:choose>
    	        </c:forEach>
		</select>
		
		<label for="cliente"> Cliente:</label>
		<select required="true" name="cliente" id="cliente">
				 <c:forEach items="${clientes}" var="cliente">
				 <c:choose>
                    <c:when test="${cliente.id == reserva.cliente.id}">
                        <option value="${cliente.id}" selected > <c:out value= "${cliente.user.username}" > </c:out></option>
                    </c:when>
                    <c:otherwise>
                        <option value="${cliente.id}"> ${cliente.user.username}</option>
                    </c:otherwise>
               	 </c:choose>
    	        </c:forEach>
		</select>
		
		
		<label for="trabajador"> Trabajador:</label>
		<select required  name="trabajador" id="trabajador">
		
			<option value="" > <c:out value="null"></c:out></option> <!--  Si no hay un trabajador asociado todavía, se mostrará como null -->
		
				 <c:forEach items="${trabajadores}" var="trabajador">
				 <c:choose>
                    <c:when test="${trabajador.id == reserva.trabajador.id}">
                        <option value="${trabajador.id}" selected > <c:out value="${trabajador.user.username}" ></c:out></option>
                    </c:when>
                    <c:otherwise>
                        <option value="${trabajador.id}"> ${trabajador.user.username}</option>
                    </c:otherwise>
               	 </c:choose>
    	        </c:forEach>
		</select>
		
		<label for="automovil"> Automóvil:</label>
		<select required  name="automovil" id="automovil">
			
				
				<option value="" > <c:out value="null"></c:out></option> <!--  Si no hay un auto asociado todavía, se mostrará como null -->
		
				 <c:forEach items="${automoviles}" var="automovil">
				 <c:choose>
                    <c:when test="${automovil.id == reserva.automovil.id and not empty reserva.automovil}">
                        <option value="${automovil.id}" selected > <c:out value="${automovil.marca} ${automovil.modelo}"></c:out></option>
                    </c:when>
                    <c:otherwise>
                        <option value="${automovil.id}"  > <c:out value="${automovil.marca} ${automovil.modelo}"></c:out></option>
                    </c:otherwise>
               	 </c:choose>
    	        </c:forEach>
		</select>
		
		
		<br><br>
	<span> &nbsp;&nbsp;<button class="btn btn-default" type="submit" name="action" value="guardarReserva">Guardar reserva</button> </span> 
     	 
     	 
     	 
     	 
    	
    </form:form>    
    
</petclinic:layout>