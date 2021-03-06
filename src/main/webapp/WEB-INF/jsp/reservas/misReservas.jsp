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
	<h2>Listado de Reservas</h2>
	
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
            <th style="width: 200px;">¿Cancelar?</th>
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
                 <spring:url value="/reservas/reservaMiFactura/{reservaId}" var="reservaFacturaEditUrl">
                   <spring:param name="reservaId" value="${reserva.id}"/>                  
                </spring:url>
                  <a class="editFacturaReserva" href="${fn:escapeXml(reservaFacturaEditUrl)}"> Ver</a>
                </td>
                              
                <td>
              <spring:url value="/clientes/myReservas/cancelar/{reservaId}" var="cancelarReservaUrl">
                <spring:param name="reservaId" value="${reserva.id}"/>
                </spring:url>
                <a class="btn btn-danger" href="${fn:escapeXml(cancelarReservaUrl)}"><span class="glyphicon glyphicon-remove" aria-hidden="false"></span></a>
                </td>     
            
        </c:forEach>
        </tbody>
    </table>
    
   
</petclinic:layout>