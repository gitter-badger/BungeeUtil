package dev.wolveringer.BungeeUtil.packets;

import java.util.Iterator;
import java.util.UUID;

import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;
import dev.wolveringer.BungeeUtil.gameprofile.GameProfile;
import dev.wolveringer.BungeeUtil.gameprofile.Property;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOutEntityAbstract;
import dev.wolveringer.api.datawatcher.DataWatcher;
import dev.wolveringer.api.position.Location;
import dev.wolveringer.nbt.MathHelper;
import dev.wolveringer.packet.PacketDataSerializer;
import dev.wolveringer.packet.PacketDataSerializer_v1_7;

public class PacketPlayOutNamedEntitySpawn extends PacketPlayOutEntityAbstract implements PacketPlayOut{
	private GameProfile p = new GameProfile(UUID.randomUUID(), ""+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"cError: 201");
	private UUID uuid;
	private Location loc;
	private byte yaw; // yaw / 256*360
	private byte pitch; // pitch / 256*360
	private int item_id;
	private DataWatcher data;

	public PacketPlayOutNamedEntitySpawn() {
		super(0x0C);
	}

	public PacketPlayOutNamedEntitySpawn(int id, UUID uuid, Location loc, int hand, DataWatcher w) {
		super(0x0C);
		setId(id);
		this.uuid = uuid;
		this.loc = loc;
		this.yaw = ((byte) (int) (loc.getYaw() * 256.0F / 360.0F));
		this.pitch = ((byte) (int) (loc.getPitch() * 256.0F / 360.0F));
		this.item_id = hand;
		this.data = w;
		this.p = new GameProfile(uuid, ""+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"cError:-202");
	}

	@SuppressWarnings("deprecation")
	public PacketPlayOutNamedEntitySpawn(int id, GameProfile profile, Location loc, int hand, DataWatcher w) {
		super(0x0C);
		setId(id);
		this.p = profile;
		if(profile == null)
			throw new NullPointerException("Profile cant be null");
		this.uuid = profile.getId();
		this.loc = loc;
		this.yaw = ((byte) (int) (loc.getYaw() * 256.0F / 360.0F));
		this.pitch = ((byte) (int) (loc.getPitch() * 256.0F / 360.0F));
		this.item_id = hand;
		this.data = w;
	}

	@SuppressWarnings("deprecation")
	public PacketPlayOutNamedEntitySpawn(int id, UUID uuid, Location loc, GameProfile g, int hand, DataWatcher w) {
		super(0x0C);
		setId(id);
		this.p = g;
		this.uuid = uuid;
		this.loc = loc;
		this.yaw = ((byte) (int) (loc.getYaw() * 256.0F / 360.0F));
		this.pitch = ((byte) (int) (loc.getPitch() * 256.0F / 360.0F));
		this.item_id = hand;
		this.data = w;
	}

	public void read(PacketDataSerializer paramPacketDataSerializer) {
		setId(paramPacketDataSerializer.readVarInt());
		this.uuid = paramPacketDataSerializer.readUUID();
		if(getBigVersion() == BigClientVersion.v1_9 || getBigVersion() == BigClientVersion.v1_10)
			loc = new Location(paramPacketDataSerializer.readDouble(), paramPacketDataSerializer.readDouble(), paramPacketDataSerializer.readDouble());
		else
			loc = new Location((double) paramPacketDataSerializer.readInt()/32D,(double) paramPacketDataSerializer.readInt()/32D,(double) paramPacketDataSerializer.readInt()/32D);
		this.yaw = paramPacketDataSerializer.readByte();
		this.pitch = paramPacketDataSerializer.readByte();
		if(getBigVersion() == BigClientVersion.v1_8)
			this.item_id = paramPacketDataSerializer.readShort();
		this.data = DataWatcher.createDataWatcher(getBigVersion(),paramPacketDataSerializer);
	}

	@SuppressWarnings("rawtypes")
	public void write(PacketDataSerializer paramPacketDataSerializer) {
		paramPacketDataSerializer.writeVarInt(getId());
			paramPacketDataSerializer.writeUUID(this.uuid);
		if(getBigVersion() == BigClientVersion.v1_9 || getBigVersion() == BigClientVersion.v1_10){
			paramPacketDataSerializer.writeDouble(loc.getX());
			paramPacketDataSerializer.writeDouble(loc.getY());
			paramPacketDataSerializer.writeDouble(loc.getZ());
		}
		else
		{
			paramPacketDataSerializer.writeInt((int)MathHelper.floor(loc.getX() * 32.0D));
			paramPacketDataSerializer.writeInt((int)MathHelper.floor(loc.getY() * 32.0D));
			paramPacketDataSerializer.writeInt((int)MathHelper.floor(loc.getZ() * 32.0D));
		}
		paramPacketDataSerializer.writeByte(this.yaw);
		paramPacketDataSerializer.writeByte(this.pitch);
		if(getBigVersion() == BigClientVersion.v1_8)
			paramPacketDataSerializer.writeShort(this.item_id);
		this.data.write(paramPacketDataSerializer);
	}

	public DataWatcher getData() {
		return data;
	}

	public GameProfile getGameProfile() {
		return p;
	}

	public void setGameProfile(GameProfile p) {
		this.p = p;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public Location getLocation() {
		return loc;
	}
	public void setLocation(Location loc) {
		this.loc = loc;
	}

	public int getItem_id() {
		return item_id;
	}

	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}

	public void setData(DataWatcher data) {
		this.data = data;
	}
	
	public byte getYaw() {
		return yaw;
	}

	public void setYaw(byte yaw) {
		this.yaw = yaw;
	}

	public byte getPitch() {
		return pitch;
	}

	public void setPitch(byte pitch) {
		this.pitch = pitch;
	}
}
