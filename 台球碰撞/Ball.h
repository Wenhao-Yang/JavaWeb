#pragma once
#include "proj.win32\b2Sprite.h"

enum {
	kColorYellow,
	kColorWhite
};

class Ball : public b2Sprite {
public:
	~Ball();
	Ball(HelloWorldScene *game, int type, CCPoint position, int color);
	static Ball* create(HelloWorldScene *game, int type, CCPoint position, int color);
	virtual void update(float dt);
private:
	void initBall();
	CCPoint _startPosition;

	int _color;
};