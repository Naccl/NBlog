package top.naccl.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Telegram新消息
 *
 * @author: Naccl
 * @date: 2022-01-24
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TgMessage {
	@JsonProperty("update_id")
	private String updateId;
	private Message message;

	@NoArgsConstructor
	@Getter
	@Setter
	@ToString
	public class Message {
		@JsonProperty("message_id")
		private String messageId;
		private From from;
		private Chat chat;
		private String date;
		private String text;

		@NoArgsConstructor
		@Getter
		@Setter
		@ToString
		public class From {
			private String id;
			@JsonProperty("is_bot")
			private Boolean isBot;
			@JsonProperty("first_name")
			private String firstName;
			private String username;
			@JsonProperty("language_code")
			private String languageCode;
		}

		@NoArgsConstructor
		@Getter
		@Setter
		@ToString
		public class Chat {
			private String id;
			@JsonProperty("first_name")
			private String firstName;
			private String username;
			private String type;
		}
	}
}
