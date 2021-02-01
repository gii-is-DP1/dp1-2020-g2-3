
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="Reserva">
    
     <p>
    	<a href="/reservas/new" class="btn  btn-success"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span>Solicitar nueva reserva</a>
    </p>
    <br> 
	<h2>Reservas</h2>
	
    <table id="reservasTable" class="table table-striped">
        <thead>
        <tr>
         	<th style="width: 100px;">Cliente</th>
          	<th style="width: 100px;">Origen</th>
            <th style="width: 200px;">Destino</th>
            <th style="width: 200px;">Fecha Salida</th>
            <th style="width: 100px;">Hora Salida</th>
            <th style="width: 100px;">Fecha llegada</th>
            <th style="width: 100px;">Hora llegada</th>
            <th style="width: 150px;">Plazas Ocupadas</th>
            <th style="width: 200px;">Precio total</th>
            <th style="width: 200px;">Estado Reserva</th>
             <th style="width: 200px;">Factura</th>
             <th style="width: 200px;">Detalles/Editar</th>
              <th style="width: 200px;">Eliminar</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${reservas}" var="reserva">
            <tr>
            	<td>
                    <c:out value="${reserva.cliente.user.username}"/>
                </td>
                <td>
                    <c:out value="${reserva.ruta.origenCliente}"/>
                </td>
                <td>
                    <c:out value="${reserva.ruta.destinoCliente}"/>
                </td>
                <td>
                    <c:out value="${reserva.fechaSalida}"/>
                </td>
                 <td>
                    <c:out value="${reserva.horaSalida}"/>
                </td>
                 <td>
                    <c:out value="${reserva.fechaLlegada}"/>
                </td>
                 <td>
                    <c:out value="${reserva.horaLlegada}"/>
                </td>
                  <td>
                    <c:out value="${reserva.plazas_Ocupadas}"/>
                </td>
                  <td>
                    <c:out value="${reserva.precioTotal}"/>
                </td>
                  <td>
                    <c:out value="${reserva.estadoReserva}"/>
                </td>
             
                 <td>
                   <spring:url value="/reservas/reservaFactura/{reservaId}" var="reservaFacturaEditUrl">
                   <spring:param name="reservaId" value="${reserva.id}"/>                  
                </spring:url>
                  <a class="editFacturaReserva" href="${fn:escapeXml(reservaFacturaEditUrl)}"> Ver</a>
                
               
                </td>
                                 
                <td>
                   <spring:url value="/reservas/edit/{reservaId}" var="reservaEditUrl">
                   <spring:param name="reservaId" value="${reserva.id}"/>                  
                </spring:url>
                  <a class="editReserva" href="${fn:escapeXml(reservaEditUrl)}"> <img alt="" id="edit" src="/resources/images/edit.png" style="width: 45px"></a>
                
               
                </td>
                <td>
                 <spring:url value="/reservas/delete/{reservaId}" var="reservaDeleteUrl">
                <spring:param name="reservaId" value="${reserva.id}"/>
                </spring:url>
				<a class="deleteReserva" href="${fn:escapeXml(reservaDeleteUrl)}"> <img alt="" id="delete" src="/resources/images/delete.png" style="width: 45px"></a>
                </td>
            </tr>
            <td>
            
        </c:forEach>
        </tbody>
    </table>
    
   
</petclinic:layout>
