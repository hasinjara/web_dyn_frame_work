<%@page import="table.*" %>

<% 
// out.println(request.getAttribute("Dept"));
    Dept dept = (Dept)request.getAttribute("Dept");
    out.println(dept.getName());
    
%>
<form method="post" action = "Dept-Id">
   <input type="number" name="numero" placeholder="numero">
   <input type="text" name="nom" placeholder="nom">
   <button type="submit">Valider</button>
</form>