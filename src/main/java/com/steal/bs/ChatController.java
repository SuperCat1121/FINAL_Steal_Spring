package com.steal.bs;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.steal.bs.chatting.ChatBiz;
import com.steal.bs.chatting.ChatDto;
import com.steal.bs.chatting.ChatMemberDto;
import com.steal.bs.model.ChattingDto;

@Controller
public class ChatController {

	@Autowired
	ChatBiz chatBiz;
	
	@RequestMapping("chat.do")
	public String chat(Model model, HttpServletRequest request) throws Exception {
		
		ChattingDto userDto = (ChattingDto)request.getSession().getAttribute("login");
		
		ChatMemberDto chatMember = chatBiz.getRoomMember(new ChatMemberDto(0, userDto.getMain_id(), "",""));
		
		if(chatMember == null) {
			chatBiz.addRoomMember(new ChatMemberDto(0, userDto.getMain_id(),"all","all"));
			
			chatMember = chatBiz.getRoomMember(new ChatMemberDto(0, userDto.getMain_id(), "",""));
			
		} else {
			chatBiz.updateRoomMember(new ChatMemberDto(0,userDto.getMain_id(),"all",""));
		}
		
		model.addAttribute("room", "all");
		
		return "chattingHome";
	}
	
	
	@RequestMapping("createChatRoom.do")
	public String createChatRoom(Model model, HttpServletRequest request, ChatDto chatDto) throws Exception {
		
		ChattingDto userDto = (ChattingDto)request.getSession().getAttribute("login");
		
		ChatDto checkdto = chatBiz.checkRoom(chatDto.getName());
		if(checkdto == null) {
			chatBiz.createChatRoom(chatDto);
		}
		
		chatBiz.updateRoomMember(new ChatMemberDto(0, userDto.getMain_id(), chatDto.getName(),""));
		
		model.addAttribute("room",chatDto.getName());
		
		return "chattingHome";
	}
	
	@RequestMapping("checkRoom.do")
	@ResponseBody
	public int checkRoom(Model model, String name) throws Exception {
		
		ChatDto dto = chatBiz.checkRoom(name);
		
		if(dto == null) {
			return 1;
		} else {
			return 0;
		}
	}
	
	@RequestMapping("moveChatRoom.do")
	public String moveChatRoom (Model model, HttpServletRequest request, String roomName) throws Exception {
		
		ChattingDto userDto = (ChattingDto)request.getSession().getAttribute("login");
		
		chatBiz.updateRoomMember(new ChatMemberDto(0, userDto.getMain_id(), roomName, ""));

		model.addAttribute("room", roomName);
		
		return "chattingHome";
	}
}
