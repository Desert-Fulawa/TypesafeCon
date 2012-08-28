package models

import anorm._
import org.joda.time.DateTime
import anorm.SqlParser._
import anorm._
import play.api.Play.current
import play.api.db.DB
import play.api.Logger

case class Speaker(var id: Pk[Long],
				name: String,
				title: String,
				about: String,
				email: Option[String],
				twitter: Option[String],
				url: Option[String]) {
	
	/** Inserts the speaker into the DB
	  */
	def create = {
		Logger.debug("Creating Speaker " + this)
		
		DB.withConnection { implicit connection =>
			val id = SQL("""insert into speaker (name, title, about, email, twitter, url) 
							values ({name}, {title}, {about}, {email}, {twitter}, {url})""").on(
						'name -> name,
						'title -> title,
						'about -> about,
						'email -> email,
						'twitter -> twitter,
						'url -> url).executeInsert()
						
			id.map {
				case id => {
					this.id = Id(id)
					this
				}
			}
		}
	}
}

object Speaker {
	
	private val speaker = {
		get[Pk[Long]]("id") ~
		get[String]("name") ~
		get[String]("title") ~
		get[String]("about") ~
		get[Option[String]]("email") ~ 
		get[Option[String]]("twitter") ~ 
		get[Option[String]]("url") map {
			case id ~ name ~ title ~ about ~ email ~ twitter ~ url => 
				Speaker(id, name, title, about, email, twitter, url)
		}
	}
		
	/** Fetches all Events
	  */
	def findAll = {
		DB.withConnection { implicit connection =>
			SQL("select * from speaker").as(speaker *)
		}
	}
}