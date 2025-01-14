package com.jeanpiress.ProjetoBarbearia.api.exceptionHandlers;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler{

	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request){

		ProblemType problemType = ProblemType.ACESSO_NEGADO;

		HttpStatus status = HttpStatus.FORBIDDEN;

		String detail = "Você não possui permissão para executar essa operação";


		Problema problema = createProblemBuilder(status, problemType, detail).build();

		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}

	@ExceptionHandler(EmailExistenteException.class)
	public ResponseEntity<Object> handleEmailExistenteException(EmailExistenteException ex, WebRequest request){

		ProblemType problemType = ProblemType.EMAIL_EXISTENTE;

		HttpStatus status = HttpStatus.BAD_REQUEST;

		String detail = "O email fornecido já foi cadastrado";


		Problema problema = createProblemBuilder(status, problemType, detail).build();

		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}

	@ExceptionHandler(SenhaAtualIncorretaException.class)
	public ResponseEntity<Object> handleSenhaAtualIncorretaException(SenhaAtualIncorretaException ex, WebRequest request){

		ProblemType problemType = ProblemType.SENHA_INCORRETA;

		HttpStatus status = HttpStatus.BAD_REQUEST;

		String detail = "A senha atual fornecida esta incorreta";


		Problema problema = createProblemBuilder(status, problemType, detail).build();

		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}

	@ExceptionHandler(ConferenciaSenhaException.class)
	public ResponseEntity<Object> handleConferenciaSenhaException(ConferenciaSenhaException ex, WebRequest request){

		ProblemType problemType = ProblemType.CONFERENCIA_SENHA_INCORRETA;

		HttpStatus status = HttpStatus.BAD_REQUEST;

		String detail = "Confirme sua senha corretamente";


		Problema problema = createProblemBuilder(status, problemType, detail).build();

		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		return ResponseEntity.status(status).headers(headers).build();
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

		ProblemType problemType = ProblemType.DADOS_INVALIDOS;
		String detail = "Um ou mais campos estão invalidos. Faça o preenchimento correto e tente novamente";

		BindingResult bindResult = ex.getBindingResult();

		List<Problema.Field> problemFilds = bindResult.getFieldErrors().stream()
				.map(fieldError -> {
					String message = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
					return Problema.Field.builder()
						.name(fieldError.getField())
						.userMessage(message)
						.build();
				})
				.collect(Collectors.toList());

		Problema problema = createProblemBuilder(status, problemType, detail)
				.fields(problemFilds)
				.build();

		return handleExceptionInternal(ex, problema, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		Throwable rootCause = ExceptionUtils.getRootCause(ex);

		if(rootCause instanceof InvalidFormatException) {
			return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
		}else if(rootCause instanceof PropertyBindingException) {
			return handlePropertyBindingException((PropertyBindingException) rootCause, headers, status, request);
		}

		ProblemType problemType = ProblemType.MENSAGEM_INCOMPEENSIVEL;
		String detail = "O corpo da requisição esta invalido, verifique erro de sintaxe";

		Problema problema = createProblemBuilder(status, problemType, detail).build();

		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(
			TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		if(ex instanceof MethodArgumentTypeMismatchException) {
			return handleMethodArgumentTypeMismatch(
					(MethodArgumentTypeMismatchException) ex, headers, status, request);
		}

		return super.handleTypeMismatch(ex, headers, status, request);
	}



	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(
			NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;

		String detail = String.format("O recurso '%s', que você tentou acessar, é inexistente.",
				ex.getRequestURL());

		Problema problema = createProblemBuilder(status, problemType, detail).build();

		return handleExceptionInternal(ex, problema, headers, status, request);
	}

	@ExceptionHandler(Exception.class)
	private ResponseEntity<Object> handleExcption(Exception ex, WebRequest request){

		ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;

		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

		String detail = "Ocorreu um erro interno inesperado no sistema. Tente novamente e se o"
				+ "problema persistir, entre em contato com o administrador do sistema";

		ex.printStackTrace();

		Problema problema = createProblemBuilder(status, problemType, detail).build();

		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}

	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> tratarEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex, WebRequest request){
		HttpStatus status = HttpStatus.NOT_FOUND;
		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		String detail = ex.getMessage();

		Problema problema = createProblemBuilder(status, problemType, detail).build();

		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);

	}

	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> tratarNegocioException(NegocioException ex, WebRequest request){
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.ERRO_NEGOCIO;
		String detail = ex.getMessage();

		Problema problema = createProblemBuilder(status, problemType, detail).build();


		return handleExceptionInternal(ex, problema, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);

	}

	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<?> tratarEntidadeEmUso(EntidadeEmUsoException ex, WebRequest request){
		HttpStatus status = HttpStatus.CONFLICT;
		ProblemType problemType = ProblemType.ENTIDADE_EM_USO;
		String detail = ex.getMessage();

		Problema problema = createProblemBuilder(status, problemType, detail).build();

		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}


	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		if(body == null) {
		body = Problema.builder()
				.title(status.getReasonPhrase());
		}else if(body instanceof String) {
			body = Problema.builder()
					.title((String)body);
		}

		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	private ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
																	HttpHeaders headers, HttpStatus status, WebRequest request) {

		ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;

		String detail = String.format("O parâmetro de URL '%s' recebeu o valor '%s', "
						+ "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
				ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

		Problema problema = createProblemBuilder(status, problemType, detail).build();

		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}

	private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex,
																  HttpHeaders headers, HttpStatus status, WebRequest request) {
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPEENSIVEL;

		String path = joinPath(ex.getPath());

		String detail = String.format("A propriedade '%s' não existe. "
				+ "Corrija ou remova essa propriedade e tente novamente", path);

		Problema problema = createProblemBuilder(status, problemType, detail).build();

		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}


	private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex,
																HttpHeaders headers, HttpStatus status, WebRequest request) {

		ProblemType problemType = ProblemType.MENSAGEM_INCOMPEENSIVEL;

		String path = joinPath(ex.getPath());

		String detail = String.format("A propriedade '%s' recebeu o valor '%s', "
						+ "que é de um tipo invalido, corrija e informe um valor compativel com o tipo %s.",
				path, ex.getValue(), ex.getTargetType().getSimpleName());

		Problema problema = createProblemBuilder(status, problemType, detail).build();

		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}

	private ResponseEntity<Object> handleCampoObrigatorioException(CampoObrigatorioException ex,
																HttpHeaders headers, HttpStatus status, WebRequest request) {

		ProblemType problemType = ProblemType.CAMPO_OBRIGATORIO;

		String detail = ex.getMessage();

		Problema problema = createProblemBuilder(status, problemType, detail).build();

		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}


	private Problema.ProblemaBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail){
		return Problema.builder().
				title(problemType.getTitle())
				.detail(detail);
		
	}
	
	private String joinPath(List<Reference> references) {
		return references.stream()
			.map(ref -> ref.getFieldName())
			.collect(Collectors.joining("."));
	}


}
