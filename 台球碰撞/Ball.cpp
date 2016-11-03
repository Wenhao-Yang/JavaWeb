#include "proj.win32\Ball.h"
#include "proj.win32\b2Sprite.h"
#include "HelloWorldScene.h"


Ball::~Ball() {

}

Ball::Ball(HelloWorldScene *game, int type, CCPoint position, int color) :b2Sprite(game, type) {//初始化一个球
	_type = type;
	_startPosition = position;
	_color = color;
}

Ball* Ball::create(HelloWorldScene *game, int type, CCPoint position, int color) {//把创建的刚体球和精灵绑定到一起
	Ball *sprite = new Ball(game, type, position, color);
	if (sprite) {
		sprite->initBall();
		sprite->autorelease();
		return sprite;
	}
	CC_SAFE_DELETE(sprite);
	return NULL;
}

void Ball::initBall() {//初始化球的信息
	b2BodyDef bodyDef;
	bodyDef.type = b2_dynamicBody;//设置球的刚体类型为动态

	_body = _game->getWorld()->CreateBody(&bodyDef);
	_body->SetSleepingAllowed(true);
	_body->SetLinearDamping(0.7);//设置线阻尼
	_body->SetAngularDamping(0.01);//设置角阻尼

	b2CircleShape circle;
	circle.m_radius = BALL_RADIUS / PTM_RATIO;

	b2FixtureDef fixtureDef;//创建一个夹具
	fixtureDef.shape = &circle;
	fixtureDef.density = 1.0f;
	fixtureDef.restitution = 0.7;
	fixtureDef.friction = 0.01f;

	if (_type == kSpriteBall) {
		fixtureDef.filter.categoryBits = 0x0010;//自己所属的碰撞种类
	}
	else if (_type == kSpritePlayer) {
		_body->SetBullet(true);
		fixtureDef.filter.categoryBits = 0x0100;//自己所属的碰撞种类
	}

	switch (_color) {
	case kColorYellow:
		this->initWithSpriteFrameName("ball_yellow.png");
		break;
	case kColorWhite:
		this->initWithSpriteFrameName("ball_white.png");
		break;
	}

	_body->CreateFixture(&fixtureDef);//刚体和夹具绑定到一起
	_body->SetUserData(this);

	setSpritePosition(_startPosition);//设置球的位置
}

void Ball::update(float dt) {//更新刚体的位置
	if (_body && isVisible()) {
		setPositionX(_body->GetPosition().x * PTM_RATIO);
		setPositionY(_body->GetPosition().y * PTM_RATIO);
	}
}