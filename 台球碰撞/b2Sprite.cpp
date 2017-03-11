#include "proj.win32\b2Sprite.h"
#include "HelloWorldScene.h"

b2Sprite::b2Sprite(HelloWorldScene *game, int type) {//��ʼ��һ������
	_game = game;
	_type = type;
}

void b2Sprite::setSpritePosition(CCPoint position) {//���þ����λ��
	setPosition(position);

	if (_body) {
		_body->SetTransform(b2Vec2(
			position.x / PTM_RATIO,
			position.y / PTM_RATIO),
			_body->GetAngle());
	}
}

void b2Sprite::update(float dt) {//���¾����λ��

	if (_body && isVisible()) {
		setPositionX(_body->GetPosition().x * PTM_RATIO);
		setPositionY(_body->GetPosition().y * PTM_RATIO);
		setRotation(CC_RADIANS_TO_DEGREES(-1 * _body->GetAngle()));
	}
}

void b2Sprite::hide() {//�������ؾ���
	if (_body) {
		_body->SetLinearVelocity(b2Vec2_zero);
		_body->SetAngularVelocity(0);
		_body->SetTransform(b2Vec2_zero, 0.0);
		_body->SetAwake(false);
	}
}

void b2Sprite::reset() {}

float b2Sprite::mag() {
	if (_body) {
		return pow(_body->GetLinearVelocity().x, 2) +
			pow(_body->GetLinearVelocity().y, 2);
	}
	return 0.0;
}