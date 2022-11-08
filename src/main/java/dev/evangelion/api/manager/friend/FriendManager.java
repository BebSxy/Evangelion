package dev.evangelion.api.manager.friend;

import java.util.Iterator;
import java.util.ArrayList;

public class FriendManager
{
    private final ArrayList<Friend> friends;
    
    public FriendManager() {
        this.friends = new ArrayList<Friend>();
    }
    
    public ArrayList<Friend> getFriends() {
        return this.friends;
    }
    
    public Friend getFriend(final String name) {
        return this.friends.stream().filter(f -> f.getName().equals(name)).findFirst().orElse(null);
    }
    
    public boolean isFriend(final String name) {
        for (final Friend friend : this.getFriends()) {
            if (friend.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
    
    public void addFriend(final String name) {
        this.friends.add(new Friend(name));
    }
    
    public void removeFriend(final String name) {
        if (this.getFriend(name) != null) {
            this.friends.remove(this.getFriend(name));
        }
    }
    
    public void clearFriends() {
        this.friends.clear();
    }
}
