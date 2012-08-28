package controllers

import play.api.Play.current
import play.api.libs.json.Json.toJson
import play.api.libs.json.Writes
import play.api.mvc.Controller
import play.api.Logger
import play.api.data._

class APIController extends Controller {
	
	def Error(message: String) = 
		BadRequest(toJson(Map(	"status" -> "ERROR",
								"message" -> message,
								"result" -> "")))
								
	def ServerError(message: String) = 
		InternalServerError(toJson(Map(	"status" -> "ERROR",
										"message" -> message,
										"result" -> "")))
								
	def MissingParam(name: String) = Error("Missing parameter: " + name)
	
	def InvalidParam(name: String, details: String) = Error("Invalid parameter: " + name + " (" + details + ")")

	def UnauthorizedAccess = Error("Unauthorized access")
								
	def Success[A](result: A)(implicit formatter: Writes[A]) =
		Ok(toJson(Map(	"status" -> toJson("OK"),
						"message" -> toJson("Success"),
						"result" -> toJson(result))))
						
	def Success[A](list: Seq[A], listName: String)(implicit formatter: Writes[A]) =
		Ok(toJson(Map(	"status" -> toJson("OK"),
						"message" -> toJson("Success"),
						"result" -> toJson(Map(listName -> toJson(list))))))
}