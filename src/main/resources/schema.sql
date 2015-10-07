CREATE CLASS User EXTENDS V;
CREATE PROPERTY User.username STRING;
ALTER PROPERTY User.username MANDATORY TRUE;
ALTER PROPERTY User.username NOTNULL TRUE;
CREATE INDEX User.username UNIQUE_HASH_INDEX;

CREATE CLASS FriendList EXTENDS V;
CREATE PROPERTY FriendList.uuid STRING;
ALTER PROPERTY FriendList.uuid MANDATORY TRUE;
ALTER PROPERTY FriendList.uuid NOTNULL TRUE;
CREATE INDEX FriendList.uuid UNIQUE_HASH_INDEX;

CREATE CLASS Group EXTENDS V;
CREATE PROPERTY Group.uuid STRING;
ALTER PROPERTY Group.uuid MANDATORY TRUE;
ALTER PROPERTY Group.uuid NOTNULL TRUE;
CREATE INDEX Group.uuid UNIQUE_HASH_INDEX;

CREATE CLASS Has EXTENDS E;
CREATE PROPERTY Has.in LINK FriendList;
ALTER PROPERTY Has.in MANDATORY TRUE;
CREATE PROPERTY Has.out LINK User;
ALTER PROPERTY Has.out MANDATORY TRUE;

CREATE CLASS IsGroupOf EXTENDS E
CREATE PROPERTY IsGroupOf.in LINK FriendList
ALTER PROPERTY IsGroupOf.in MANDATORY TRUE
CREATE PROPERTY IsGroupOf.out LINK Group
ALTER PROPERTY IsGroupOf.out MANDATORY TRUE

CREATE CLASS IsIn EXTENDS E
CREATE PROPERTY IsIn.in LINK Group
ALTER PROPERTY IsIn.in MANDATORY TRUE
CREATE PROPERTY IsIn.out LINK User
ALTER PROPERTY IsIn.out MANDATORY TRUE
