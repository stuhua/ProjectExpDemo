�}��  C#  lv)��^�u
�� ֛�2x���yXцZ_5I%fd�j#��x�PO�����BcϢkC!�%�e&46�� �F@�����h�3����W}:fT+��52��	�B�x�]��\,��?�)�ϳ�4Ƃ�c�k��&{���ٿgb�FY^ZF�r�,^�P���ᵁ3�,6~��h�b�ĝټ�j��S�2�%Lz��Z��_lT�$ҳ^|�4�G{�h~��J��_ϢQ�ඨXd����?����S��^�&�����i�C��w9�!���a���)��.�Hb1X��iP��Q��2.�Ԏ��lG�gJ16�Ҥ--��@vC�^���<U)�����0���o=`S|�õT�!��������"z!LA��`��+��?wp�c��RR�}s��褔}��DH��ގ��ƨĮ���s����t̲؍O����]��t7�z2ۈ��[��??�MJ�odS���'�q�	!8�ʋyZ��X�l���ݙK�w��1�,�[�a�OY܃��0�W��?��C^i��eu�p��5X�������(8lɿ��>�y��豥d�et6�ձD�l�� !���~���	�J�r�h"�OSb +�Z�ռ8����0�k(�?\��۴�>�0���9�P�5		��F�5����1`I���7���,��}	�n@��<A�hL�wZ ��8Rim��<����v��@�+�rz������f_��/.R�2�fO���4w;`�_��/Ӂ �i$F�?�o�8�e����ļ�v5$oJnr9��=�� �\_=9�ʒ��h�:�w��Tj���.	T{��	�����x��jdnԪ���t��C0�#橍��0�6��<�9�t�w�����T���uH��FFh0Y�}���ߗ��zz�X*���0�2�64`�̡�~䗭4�^z�mz�H��6~�'�4�/~��f��&�]^�B��`\㦎t܋��FV�m4��M״sn��7�\H2�w�l� ��{U�X�Ǹ�V*lX�T�_�و�Mm��V8���bW�D�t-�v����9����B�-�,/�;@l�~��������_�ݝ�S<r#s?:a@�y�M��d�w0I�J���e1j�g(O1���Z[<v1�`%n�<�˖�;����mѺ�L�VR�y<��TA���_�8U�r;o*��ԃ��UKw���<�mG�y��Nc;���b��Oj��<bPO�@0���\i�~=3����֥K`g���?Jϓ������B!Ϗ?��{9|�
}XM(P9}���c�oT+�n�;����w/͵)H+�����g6��S$}�MfJ���T|h�NM(�gVf�N���Jz׉�U~���R�t�z�K����C+ʜLh�&�֔!�|["=Ң�|0��I��%P޸�񥚎�.�b��1��9�cM"ѣ���.	�}�Q&�� nM��Wj{��V�����Z����ޖ ������K��]��Z�&K��~�)�O�&����.�fޜl�+���m�u��K\+�_#_
��Oe�d	��1����9ɺ�m���&$_G�E�WfQ(��:n�����NsɵOQ4h&$<ɒ@�l
���ج5U�'*E�C��h���II��4@,�/`T+��/>���D��{�5O���1C��zq�����H�#`WbyJyy0jsmڼ��s�mVwk��O�f���cz���X3O���7�����0\N|^�.wF�m��L�C�twѧ��|>KFx��,�%��G��9��k!�A��Y��FB�FQ_`G�\7�;��U�c��=*����D&0� �L-]�:���������ER�eƥ��t�>/WǠ��K$\6�2Y��-�̍���9�x[����V��x�`פ�&�M�`I�'�Z����0w?Sb�f/��$_83r�~@
 ��+gP���QE�F�Y�ƣ
�?_G��_�8 i��z]���=Q��*�Sj�W��u��no� j����XR�,Di�i*.u��< l6�Sެ;9R�{��$ޭ�J��S��5���`g�Xn����MR���"tL�(����Pyȭ�y��Bݰѯ_��v%<$��>0�Ƹ���%ơ�+�2tz��K_��P&d!
CÎ��$fݙ����1+���i;�X���Jf>�����NRY$G�cq���H؈�,��xJ��0�^�r�et����܁�P؁��I�櫚Q2�*p�������r�٥�<%�c �����|5�ֶ��Q��a��
"�F��ϊ��I=c�ui���s���<�I��ey�����6�T/�\�<��-����x�UΜ�0�X�I�H-�C	6��sI/L�b�_��L�K�{��h��㦡�)��^{je�H�U��M�Uy^��}���R��|w���B���V$����XƱ�(%���3{��8����-A׻x3�q�ٖ`ޔ�A�.���Gj
㰺�{Y>�kn܀���#�a��V!��M��Ԍwv��[]��fl���\�f�AM��	public PlayListGroup createFromParcel(Parcel source) {
			return new PlayListGroup(source);
		}
	};

	public PlayListGroup(Parcel source) {
		setName(source.readString());
		setID(source.readLong());
		setParentID(source.readLong());
		setIsFolder(source.readInt());
		setFTI(source.readInt());
		setMusicDataLen(source.readString());
		source.readList(musicData, MusicInfo.class.getClassLoader());
	}

}
