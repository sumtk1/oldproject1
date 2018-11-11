package com.gloiot.hygo.ui.activity.shopping.Bean;

import java.io.Serializable;

/**
 * 公共实体类
 * @author chenjunsen
 * 2015年8月18日下午3:35:45
 */
public class GouwucheBean implements Serializable {
	private String Id;
	private String dianpuming;
	private boolean isChoosed;

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getDianpuming() {
		return dianpuming;
	}

	public void setDianpuming(String dianpuming) {
		this.dianpuming = dianpuming;
	}

	public boolean isChoosed() {
		return isChoosed;
	}

	public void setChoosed(boolean choosed) {
		isChoosed = choosed;
	}

	@Override
	public String toString() {
		return "GouwucheBean{" +
				"Id='" + Id + '\'' +
				", dianpuming='" + dianpuming + '\'' +
				", isChoosed=" + isChoosed +
				'}';
	}
}
