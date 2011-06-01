/**
 * 
 */
package com.bluespot.geom.offsets;

import com.bluespot.geom.Side;

/**
 * An {@link Offset4i} in integer precision.
 * 
 * @author Aaron Faanes
 * 
 */
public class Offset4i extends AbstractOffset4<Offset4i> {
	private int left;
	private int right;
	private int top;
	private int bottom;

	public static Offset4i mutable(int side) {
		return new Offset4i(true, side, side, side, side);
	}

	public static Offset4i frozen(int side) {
		return new Offset4i(false, side, side, side, side);
	}

	public static Offset4i mutable(int left, int right, int top, int bottom) {
		return new Offset4i(true, left, right, top, bottom);
	}

	public static Offset4i frozen(int left, int right, int top, int bottom) {
		return new Offset4i(false, left, right, top, bottom);
	}

	private static final Offset4i ONE = Offset4i.frozen(1);

	public static Offset4i one() {
		return ONE;
	}

	private static final Offset4i ZERO = Offset4i.frozen(0);

	public static Offset4i zero() {
		return ZERO;
	}

	private Offset4i(boolean mutable, int left, int right, int top, int bottom) {
		super(mutable);
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
	}

	public int get(Side side) {
		if (side == null) {
			throw new NullPointerException("side must not be null");
		}
		switch (side) {
		case LEFT:
			return this.getLeft();
		case RIGHT:
			return this.getRight();
		case TOP:
			return this.getTop();
		case BOTTOM:
			return this.getBottom();
		case VERTICAL:
			return this.getVertical();
		case HORIZONTAL:
			return this.getHorizontal();
		}
		throw new IllegalArgumentException("Unexpected side value: " + side);
	}

	public int getLeft() {
		return this.left;
	}

	public int setLeft(int value) {
		if (!this.isMutable()) {
			throw new UnsupportedOperationException("Offset is not mutable");
		}
		int old = this.left;
		this.left = value;
		return old;
	}

	public int addLeft(int value) {
		return this.setLeft(this.getLeft() + value);
	}

	public Offset4i addedLeft(int value) {
		Offset4i offset = this.toMutable();
		offset.addLeft(value);
		return offset;
	}

	public int getRight() {
		return this.right;
	}

	public int setRight(int value) {
		if (!this.isMutable()) {
			throw new UnsupportedOperationException("Offset is not mutable");
		}
		int old = this.right;
		this.right = value;
		return old;
	}

	public int addRight(int value) {
		return this.setRight(this.getRight() + value);
	}

	public Offset4i addedRight(int value) {
		Offset4i offset = this.toMutable();
		offset.addRight(value);
		return offset;
	}

	public int getTop() {
		return this.top;
	}

	public int setTop(int value) {
		if (!this.isMutable()) {
			throw new UnsupportedOperationException("Offset is not mutable");
		}
		int old = this.top;
		this.top = value;
		return old;
	}

	public int addTop(int value) {
		return this.setTop(this.getTop() + value);
	}

	public Offset4i addedTop(int value) {
		Offset4i offset = this.toMutable();
		offset.addTop(value);
		return offset;
	}

	public int getBottom() {
		return this.bottom;
	}

	public int setBottom(int value) {
		if (!this.isMutable()) {
			throw new UnsupportedOperationException("Offset is not mutable");
		}
		int old = this.bottom;
		this.bottom = value;
		return old;
	}

	public int addBottom(int value) {
		return this.setBottom(this.getBottom() + value);
	}

	public Offset4i addedBottom(int value) {
		Offset4i offset = this.toMutable();
		offset.addBottom(value);
		return offset;
	}

	public int getVertical() {
		return this.getTop() + this.getBottom();
	}

	public void setVertical(int value) {
		this.setTop(value);
		this.setBottom(value);
	}

	public int getHorizontal() {
		return this.getLeft() + this.getRight();
	}

	public void setHorizontal(int value) {
		this.setLeft(value);
		this.setRight(value);
	}

	@Override
	public void set(Offset4i offset) {
		this.setLeft(offset.getLeft());
		this.setRight(offset.getRight());
		this.setTop(offset.getTop());
		this.setBottom(offset.getBottom());
	}

	public void set(int value) {
		this.setLeft(value);
		this.setRight(value);
		this.setTop(value);
		this.setBottom(value);
	}

	public void set(int left, int right, int top, int bottom) {
		this.setLeft(left);
		this.setRight(right);
		this.setTop(top);
		this.setBottom(bottom);
	}

	@Override
	public void set(Side side, Offset4i offset) {
		if (side == null) {
			throw new NullPointerException("side must not be null");
		}
		if (offset == null) {
			throw new NullPointerException("offset must not be null");
		}
		switch (side) {
		case LEFT:
			this.setLeft(offset.getLeft());
			return;
		case RIGHT:
			this.setRight(offset.getRight());
			return;
		case TOP:
			this.setTop(offset.getTop());
			return;
		case BOTTOM:
			this.setBottom(offset.getBottom());
			return;
		case VERTICAL:
			this.setTop(offset.getTop());
			this.setBottom(offset.getBottom());
			return;
		case HORIZONTAL:
			this.setLeft(offset.getLeft());
			this.setRight(offset.getRight());
			return;
		}
		throw new IllegalArgumentException("Side is invalid");
	}

	public void set(Side side, int value) {
		if (side == null) {
			throw new NullPointerException("side must not be null");
		}
		switch (side) {
		case LEFT:
			this.setLeft(value);
			return;
		case RIGHT:
			this.setRight(value);
			return;
		case TOP:
			this.setTop(value);
			return;
		case BOTTOM:
			this.setBottom(value);
			return;
		case VERTICAL:
			this.setTop(value);
			this.setBottom(value);
			return;
		case HORIZONTAL:
			this.setLeft(value);
			this.setRight(value);
			return;
		}
		throw new IllegalArgumentException("Side is invalid");
	}

	@Override
	public void add(Offset4i offset) {
		if (offset == null) {
			throw new NullPointerException("offset must not be null");
		}
		this.addLeft(offset.getLeft());
		this.addRight(offset.getRight());
		this.addTop(offset.getTop());
		this.addBottom(offset.getBottom());
	}

	public void add(int value) {
		this.addLeft(value);
		this.addRight(value);
		this.addTop(value);
		this.addBottom(value);
	}

	@Override
	public void add(Side side, Offset4i offset) {
		if (side == null) {
			throw new NullPointerException("side must not be null");
		}
		if (offset == null) {
			throw new NullPointerException("offset must not be null");
		}
		switch (side) {
		case LEFT:
			this.addLeft(offset.getLeft());
			return;
		case RIGHT:
			this.addRight(offset.getRight());
			return;
		case TOP:
			this.addTop(offset.getTop());
			return;
		case BOTTOM:
			this.addBottom(offset.getBottom());
			return;
		case VERTICAL:
			this.addTop(offset.getTop());
			this.addBottom(offset.getBottom());
			return;
		case HORIZONTAL:
			this.addLeft(offset.getLeft());
			this.addRight(offset.getRight());
			return;
		}
		throw new IllegalArgumentException("Side is invalid");
	}

	public void add(Side side, int value) {
		if (side == null) {
			throw new NullPointerException("side must not be null");
		}
		switch (side) {
		case LEFT:
			this.addLeft(value);
			return;
		case RIGHT:
			this.addRight(value);
			return;
		case TOP:
			this.addTop(value);
			return;
		case BOTTOM:
			this.addBottom(value);
			return;
		case VERTICAL:
			this.addTop(value);
			this.addBottom(value);
			return;
		case HORIZONTAL:
			this.addLeft(value);
			this.addRight(value);
			return;
		}
		throw new IllegalArgumentException("Side is invalid");
	}

	public Offset4i added(int value) {
		Offset4i result = this.toMutable();
		result.add(value);
		return result;
	}

	public Offset4i added(Side side, int value) {
		if (side == null) {
			throw new NullPointerException("side must not be null");
		}
		Offset4i result = this.toMutable();
		result.add(side, value);
		return result;
	}

	@Override
	public void clear() {
		this.set(0);
	}

	@Override
	public void clear(Side side) {
		this.set(side, 0);
	}

	@Override
	public Offset4i toMutable() {
		return Offset4i.mutable(left, right, top, bottom);
	}

	@Override
	public Offset4i toFrozen() {
		if (!this.isMutable()) {
			return this;
		}
		return Offset4i.frozen(left, right, top, bottom);
	}

	@Override
	public boolean same(Offset4i offset) {
		if (offset == null) {
			return false;
		}
		return this.getLeft() == offset.getLeft() &&
				this.getRight() == offset.getRight() &&
				this.getTop() == offset.getTop() &&
				this.getBottom() == offset.getBottom();
	}

}
