<html>

<head>
<title>Formulario de registro</title>
</head>

<body>

<center>

<h3>Cambridge English Center</h3>

<h4>Formulario de Registro</h4>

</center>

<hr>

Sea tan amable de rellenar el siguiente formulario.

<br>

<FORM ACTION="http://localhost:8080/userpreferences/services/question/answer" METHOD="POST">
	
	QuestionID: <BR><INPUT TYPE="text" NAME="question_id"> <br>
	
	Email: <BR><INPUT TYPE="text" NAME="person_email"> <br>

	Idioma: <BR> <INPUT TYPE="text" NAME="language"> <br>

	Respuestas: <BR> <INPUT TYPE="text" NAME="answers"> <br>

	Comentarios:<br> <TEXTAREA NAME="comment" ROWS="7" COLS="40"></TEXTAREA> 
	<INPUT TYPE="submit" VALUE="Enviar datos"> </FORM>

</body>

</html>