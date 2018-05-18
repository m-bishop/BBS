package com.coredumpproject.coredump.model;

import com.coredumpproject.coredump.Message;

import javax.persistence.*;
import javax.websocket.Session;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@XmlRootElement
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", updatable = false, nullable = false)
	private String userID;

	@Version
	@Column(name = "version")
	private int version;

	@Column
	@Lob
	private Item avatar;

	//@Column
	//@Lob
	@Transient
	private Item container;

	@Transient
	private Session session;

	@Transient
	private List<Message> messages = Collections
			.synchronizedList(new ArrayList());

	@Column
	private String handle;

	public void sendMessage(Message message) throws IOException {
		if (session != null) {
			session.getBasicRemote().sendText(message.toJSON());
		} else {
			synchronized (this.messages) {
				System.out.println("Adding message to user stored messages.");
				messages.add(message);
			}
		}

	}

	public List<Message> readMessages() {
		List<Message> rList = new ArrayList<>();
		synchronized (messages) {
			rList.addAll(messages);
			messages.clear();
		}
		return rList;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof User)) {
			return false;
		}
		User other = (User) obj;
		if (userID != null) {
			if (!userID.equals(other.userID)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userID == null) ? 0 : userID.hashCode());
		return result;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Item getAvatar() {
		return avatar;
	}

	public void setAvatar(Item avatar) {
		this.avatar = avatar;
	}

	public Item getContainer() {
		return container;
	}

	public void setContainer(Item container) {
		this.container = container;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (userID != null && !userID.trim().isEmpty())
			result += "userID: " + userID;
		if (handle != null && !handle.trim().isEmpty())
			result += ", handle: " + handle;
		return result;
	}
}