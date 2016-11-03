#pragma once
#define PTM_RATIO 32.0
#define BALL_RADIUS 20.0

#include "cocos2d.h"
#include "Box2D/Box2D.h"



class HelloWorldScene;
enum {
	kSpritePlayer,
	kSpriteBall,
	kSpriteCue,
	kSpritePocket
};

USING_NS_CC;

class b2Sprite : public CCSprite {
public:
	b2Sprite(HelloWorldScene *game, int type);

	CC_SYNTHESIZE(b2Body*, _body, Body);
	CC_SYNTHESIZE(HelloWorldScene*, _game, Game);
	CC_SYNTHESIZE(int, _type, Type);

	virtual void setSpritePosition(CCPoint position);
	virtual void update(float dt);
	virtual void hide(void);
	virtual void reset(void);
	virtual float mag();
};
