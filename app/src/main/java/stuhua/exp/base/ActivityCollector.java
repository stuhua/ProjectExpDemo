�}J�  _  K����u
��4֛�,,
��x�`������"�:*Ǝ� C��5s4��Ի�Kv�;U6jb�K������vʤ΃�@����f�-yegqf~��^�UZ��8�n�y��Q-��;�<�[jwd/�_����F��[���ZzPO��oJ��3n�;�oYu	&I�I[� e*�q�	��wY�=3�t��RpY����qB�%_бV	��A��c�����
wp��S��tY]��t)ǡQO��(��#���n�K�V����G������e��=��Kϙ�8	���a&��Dzs��ί݁ݓ�@vC�^���<U)�����0���o=`S|�õT�!��������"z!LA��`��+��?wp�c��RR�}s��褔}��DH��ގ��ƨĮ���s����t̲؍O����]��t7�z2ۈ��[��??�MJ�odS���'�q�	!8�ʋyZ��X�l�(p>���{b�a��owE��_�%��I
)�����rԆ7����I&]��E�$Lh��[���ɰؘJD��ç�d:�^$��DOo��Ry��m�:�F�Z��3CҊǅ-�i$Y)�8��|�vBx��U������Tgx��\�l4���_,l�Fuh5����"2��O��6m��Ub���p�O&��!�гϞm�O�
~��d�qاW٦r<&��k��\#od����J�e8��,`�����H���Z����M��0Ƌ/ڑ�S�n\��CGy|��"�`bj�v�Q����������zI�IzO�빦7��"ķ���(-�HJ�L1��|��+�M�.&W?n���!L �(�s��TI� ����?MwP�����5�|�(�$����i�ɀ|����
x�.새��*�D5����]���Q��7ٌs��&�pmD�<V�>w
 &4��.�>�Bs�w���zYWޜ���k,�����1W�����.���l�,�[�������O�9�3�9��덶+���i1GX���M1U�E��W�_��_�S�P�*�����'1�D{C�7�����:�Xn��[X{|ĂvZI	�bA���&OV������� ����IL��ʏ�e��u5~.;G��έ�\�Z"�ae7�Y!IX����&�!&�Ck����W� ��ӂ&����|U�ɱ�Q����a��u�/�����ef|��;�h\О�e8`������Q�耨Q$)F�nm��Ҽ��kFA���4��>���F� 'iŨ�D��2�CH��k��r@��B���w��?y���H��"�G� B������+���ը�Z��r����][�J|�@$1,����y��̚/7�:R�����K`E��oͧ�+L� Fy�A��	�T9�}�~Ѫ�|�����Y�b�.�ޥ�ϙZ�Z��Qn��"[��!�B�h2�Un����p�������M�V'�r�9�O�W���㤮�Zm�9|���Yf (activities.containsValue(activity)) {
			activities.remove(activity.getClass());
		}
	}

	/**
	 * 移除所有的Activity
	 */
	public static void removeAllActivity() {
		if (activities != null && activities.size() > 0) {
			Set<Entry<Class<?>, Activity>> sets = activities.entrySet();
			for (Entry<Class<?>, Activity> s : sets) {
				if (!s.getValue().isFinishing()) {
					s.getValue().finish();
				}
			}
		}
		activities.clear();
	}
}