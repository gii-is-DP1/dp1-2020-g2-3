<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="reservas">
    <h2>Factura</h2>

    <table id="facturasTable" class="table table-striped">
        <thead>
        <tr>
        	<th style="width: 150px;">Cliente</th>
            <th style="width: 150px;">Precio Total</th>
            <th style="width: 200px;">IVA Repercutido</th>
            <th style="width: 200px;">Precio Distancia</th>
            <th style="width: 200px;">Precio Extra Espera</th>
            <th style="width: 200px;">Base Imponible</th>
            <th style="width: 200px;">Tarifa</th>
           
        </tr>
        </thead>
        <tbody>
        
            <tr>
            
            	 <td>
                    <c:out value="${reserva.cliente.user.username}"/>
                </td>
                <td>
                    <c:out value="${reserva.precioTotal}"/>
                </td>
                <td>
                    <c:out value="${Math.round((tarifa.porcentajeIvaRepercutido * 0.01 * reserva.precioTotal)*100.0)/100.0}"/>
                </td>
                 <td>
                    <c:out value="${Math.round((tarifa.precioPorKm * reserva.numKmTotales)*100.0)/100.0}"/>
                </td>
                 <td>
                    <c:out value="${Math.round((reserva.horasEspera * tarifa.precioEsperaPorHora)*100.0)/100.0}"/>
                </td>
                 <td>
                    <c:out value="${Math.round((reserva.precioTotal - (tarifa.porcentajeIvaRepercutido * 0.01 * reserva.precioTotal))*100.0)/100.0}"/>
                </td>
                <td>
                    <c:out value="${tarifa.precioPorKm}"/> euros/Km
                    <p> </p>
                    <c:out value="${tarifa.porcentajeIvaRepercutido}"/>% IVA repercutido
                    <p> </p>
                    <c:out value="${tarifa.precioEsperaPorHora}"/> euros/horasEspera
                </td>
               
            </tr>
       
        </tbody>
    </table>

</petclinic:layout>
