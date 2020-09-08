package top.naccl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.naccl.entity.Friend;
import top.naccl.exception.PersistenceException;
import top.naccl.mapper.FriendMapper;
import top.naccl.service.FriendService;

import java.util.Date;
import java.util.List;

/**
 * @Description: 友链业务层实现
 * @Author: Naccl
 * @Date: 2020-09-08
 */
@Service
public class FriendServiceImpl implements FriendService {
	@Autowired
	FriendMapper friendMapper;

	@Override
	public List<Friend> getFriendList() {
		return friendMapper.getFriendList();
	}

	@Override
	public List<top.naccl.model.vo.Friend> getFriendVOList() {
		return friendMapper.getFriendVOList();
	}

	@Transactional
	@Override
	public void updateFriendPublishedById(Long friendId, Boolean published) {
		if (friendMapper.updateFriendPublishedById(friendId, published) != 1) {
			throw new PersistenceException("操作失败");
		}
	}

	@Transactional
	@Override
	public void saveFriend(Friend friend) {
		friend.setViews(0);
		friend.setCreateTime(new Date());
		if (friendMapper.saveFriend(friend) != 1) {
			throw new PersistenceException("添加失败");
		}
	}

	@Transactional
	@Override
	public void updateFriend(top.naccl.model.dto.Friend friend) {
		if (friendMapper.updateFriend(friend) != 1) {
			throw new PersistenceException("修改失败");
		}
	}

	@Transactional
	@Override
	public void deleteFriend(Long id) {
		if (friendMapper.deleteFriend(id) != 1) {
			throw new PersistenceException("删除失败");
		}
	}

	@Transactional
	@Override
	public void updateViewsByNickname(String nickname) {
		if (friendMapper.updateViewsByNickname(nickname) != 1) {
			throw new PersistenceException("操作失败");
		}
	}
}
