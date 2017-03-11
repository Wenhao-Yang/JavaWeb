#include "proj.win32\Ball.h"
#include "proj.win32\b2Sprite.h"
#include "HelloWorldScene.h"


Ball::~Ball() {

}

Ball::Ball(HelloWorldScene *game, int type, CCPoint position, int color) :b2Sprite(game, type) {//��ʼ��һ����
	_type = type;
	_startPosition = position;
	_color = color;
}

Ball* Ball::create(HelloWorldScene *game, int type, CCPoint position, int color) {//�Ѵ����ĸ�����;���󶨵�һ��
	Ball *sprite = new Ball(game, type, position, color);
	if (sprite) {
		sprite->initBall();
		sprite->autorelease();
		return sprite;
	}
	CC_SAFE_DELETE(sprite);
	return NULL;
}

void Ball::initBall() {//��ʼ�������Ϣ
	b2BodyDef bodyDef;
	bodyDef.type = b2_dynamicBody;//������ĸ�������Ϊ��̬

	_body = _game->getWorld()->CreateBody(&bodyDef);
	_body->SetSleepingAllowed(true);
	_body->SetLinearDamping(0.7);//����������
	_body->SetAngularDamping(0.01);//���ý�����

	b2CircleShape circle;
	circle.m_radius = BALL_RADIUS / PTM_RATIO;

	b2FixtureDef fixtureDef;//����һ���о�
	fixtureDef.shape = &circle;
	fixtureDef.density = 1.0f;
	fixtureDef.restitution = 0.7;
	fixtureDef.friction = 0.01f;

	if (_type == kSpriteBall) {
		fixtureDef.filter.categoryBits = 0x0010;//�Լ���������ײ����
	}
	else if (_type == kSpritePlayer) {
		_body->SetBullet(true);
		fixtureDef.filter.categoryBits = 0x0100;//�Լ���������ײ����
	}

	switch (_color) {
	case kColorYellow:
		this->initWithSpriteFrameName("ball_yellow.png");
		break;
	case kColorWhite:
		this->initWithSpriteFrameName("ball_white.png");
		break;
	}

	_body->CreateFixture(&fixtureDef);//����ͼо߰󶨵�һ��
	_body->SetUserData(this);

	setSpritePosition(_startPosition);//�������λ��
}

void Ball::update(float dt) {//���¸����λ��
	if (_body && isVisible()) {
		setPositionX(_body->GetPosition().x * PTM_RATIO);
		setPositionY(_body->GetPosition().y * PTM_RATIO);
	}
}